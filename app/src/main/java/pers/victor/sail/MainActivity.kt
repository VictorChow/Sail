package pers.victor.sail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import pers.victor.ext.click
import pers.victor.ext.toast
import pers.victor.sail.library.Sail

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urls = arrayOf(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503860081156&di=3abb20239ba58e14a64e681aa86d8045&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F131218%2F234965-13121P2254999.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503903148095&di=00b1df84cb33bf9995b4960424f2c606&imgtype=0&src=http%3A%2F%2Fpic.ffpic.com%2Ffiles%2F2013%2F1222%2F1220hamjgqzmbz10.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856112&di=0faff885e90b70a0d93115d94fc2b519&src=http://img2.niutuku.com/desk/1208/2150/ntk-2150-41037.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503893419&di=37eb5f88b3534065a5e4ca233b759dc9&src=http%3A%2F%2Fpic.ffpic.com%2Ffiles%2F2015%2F0508%2F0504jhnxzazbz10_s.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503907084219&di=830f1f2e24d2adff6b54fd043bd80d10&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Dd94c0c6c5b0fd9f9b41a5d2a4d44be5f%2Fd62a6059252dd42a1a8022bb093b5bb5c9eab810.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503903148097&di=7ea79dff84f613f15381009b80f4e83a&imgtype=0&src=http%3A%2F%2Fimg-download.pchome.net%2Fdownload%2F1k0%2Fpd%2F4j%2Fo99p6g-qxc.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503866279107&di=cf581393e33619965766417d8195ab55&imgtype=0&src=http%3A%2F%2Fimg2.niutuku.com%2Fdesk%2F1208%2F1956%2Fntk-1956-32474.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503903148096&di=031d32aee6008fb74023977b67dd7b32&imgtype=0&src=http%3A%2F%2Fwapfile.desktx.com%2F7681280%2F0924%2F160924xiznrp1vgic.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856195&di=d4e238b76c3e82790a1a76aa68a45400&src=http://att.bbs.duowan.com/forum/201408/22/231039942e0z1p00eed928.jpg"
        )

        arrayOf(iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9).forEachIndexed { index, imageView ->
            Sail.with(this)
                    .load(urls[index])
                    .holder(R.mipmap.ic_launcher)
                    .fadeIn()
                    .into(imageView)
        }
        btn_download.click {
            arrayOf(iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9).forEachIndexed { index, imageView ->
                Sail.with(this)
                        .load(urls[index])
                        .holder(R.mipmap.ic_launcher)
                        .fadeIn()
                        .into(imageView)
            }
        }

        btn_remove.click {
            arrayOf(iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9).forEach {
                it.setImageBitmap(null)
            }
        }

        btn_clear.click {
            Sail.clearCache()
            toast("已清除缓存")
        }

        btn_crop.click {
            arrayOf(iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9).forEach {
                it.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        btn_fit.click {
            arrayOf(iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9).forEach {
                it.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }


        val w = 1920
        val h = 1080
        val size = (w shl 16 and 0xffff0000.toInt()) or (h and 0xffff)
    }

}
