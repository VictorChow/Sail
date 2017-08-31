# Sail
假装自己设计一个图片加载缓存库，看看自己写能遇到什么问题。

### 已实现

* **线程池**

  Executors.newFixedThreadPool

* **缓存**

  内存：LruCache

  磁盘：文件

  缓存不同尺寸

* **Options**

  占位：holder

  错误：error

  调整大小：resize

  淡入：fade in

  质量策略：quality （LOW, NORMAL, HIGH, ORIGINAL）

  缓存策略：cache （ALL, MEMORY, DISK, NONE）

* **Decode**

  HttpURLConnection

  BufferedInputStream

  BitmapFactory.decodeStream(buffer, null, opt)

### 遇到的问题

* **内存爆炸**

  一开始是直接用`BitmapFactory.decodeStream(inputStream)`先得到bitmap，再根据Options里面的quality或resize进行裁剪，然而原来的bitmap已经加载到内存里了。测试了9张图，全部加载完之后内存占用增加了80M，有点彪。

  优化后先进行缩放比例计算。`inJustDecodeBounds = true`只会获取宽高等信息（`BitmapFactory.decodeStream(buffer, null, opt)`返回为null，bitmap并未加载到内存）：

  ```kotlin
  val opt = BitmapFactory.Options()
  opt.inJustDecodeBounds = true
  BitmapFactry.decodeStream(buffer, null, opt)
  ```

  再用`opt.outWidth`和`opt.outHeight`获取宽高，通过`opt.inSampleSize = n`设置缩放（n表示原大小的1/n）：

  ```kotlin
  val pW = (opt.outWidth.toFloat() / options.width + 0.5f).toInt()
  val pH = (opt.outHeight.toFloat() / options.height + 0.5f).toInt()
  opt.inJustDecodeBounds = false
  opt.inSampleSize = if (pW > pH) pW else pH
  BitmapFactory.decodeStream(buffer, null, opt)
  ```

  修改后测试，同样的9张图片：

  | Quality  | Memory | Disk Cache |
  | -------- | :----- | ---------- |
  | LOW      | +24M   | 768K       |
  | NORMAL   | +28M   | 1.2M       |
  | HIGH     | +38M   | 2.2M       |
  | ORIGINAL | +87M   | 6.1M       |

* **两次decode导致`BitmapFactory.decodeStream()`返回null**

  因为进行了一次获取宽高，输入流中的位置改变，第二次decode会返回null，第一次decode之前调用`inputStream.mark(length)`，之后使用`inputStream.reset()`，但**InputStream不支持mark()**，最后使用了BufferedInputStream。

  ```kotlin
  private fun decode(inputStream: InputStream, length: Int): Bitmap {
      val buffer = BufferedInputStream(inputStream)
      buffer.mark(length)
      val opt = BitmapFactory.Options()
      opt.inJustDecodeBounds = true
      BitmapFactory.decodeStream(buffer, null, opt)
      val pW = (opt.outWidth.toFloat() / options.width + 0.5f).toInt()
      val pH = (opt.outHeight.toFloat() / options.height + 0.5f).toInt()
      opt.inJustDecodeBounds = false
      opt.inSampleSize = if (pW > pH) pW else pH
      buffer.reset()
      return BitmapFactory.decodeStream(buffer, null, opt)
  }
  ```

### TODO

* **~~缓存不同尺寸~~**

  ~~同一个Url的图片保存不同尺寸的缓存~~

* **生命周期绑定**

  看了Glide源码（真的看吐了），Glide往Activity里添加了一个带生命周期回调的Fragment，通过此Fragment来进行生命周期同步，有意思的设计。

  onStart()

  onPause()

  onDestroy()

* **暂停**