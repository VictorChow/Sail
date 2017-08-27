package pers.victor.sail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pers.victor.ext.click
import pers.victor.sail.library.Sail

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_download.click {
            val urls = arrayOf(
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503860081156&di=3abb20239ba58e14a64e681aa86d8045&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F131218%2F234965-13121P2254999.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856112&di=42465abee237465f8575c75a0bfb2298&src=http://img2.niutuku.com/desk/1212/2236/2236-ntk35334.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856112&di=0faff885e90b70a0d93115d94fc2b519&src=http://img2.niutuku.com/desk/1208/2150/ntk-2150-41037.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856112&di=0c877d82b5660b1bc65f0cda6268b962&src=http://tupian.enterdesk.com/2012/0613/ning/10/4001.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856112&di=ce5ec15bf5b299b1af37d7d908de49f0&src=http://img2.niutuku.com/desk/1208/2123/ntk-2123-40321.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856112&di=bea174b15b48879fd6dfa9c007774665&src=http://fj.ikafan.com/attachment/forum/201403/01/123746djjppxp97vmcpv6b.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503866279107&di=cf581393e33619965766417d8195ab55&imgtype=0&src=http%3A%2F%2Fimg2.niutuku.com%2Fdesk%2F1208%2F1956%2Fntk-1956-32474.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856195&di=314aae96a985077b1ceb778fb8053566&src=http://imgstore.cdn.sogou.com/app/a/100540002/595347.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1503856195&di=d4e238b76c3e82790a1a76aa68a45400&src=http://att.bbs.duowan.com/forum/201408/22/231039942e0z1p00eed928.jpg"
            )
            println(System.currentTimeMillis())
            arrayOf(iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9).forEachIndexed { index, imageView ->
                Sail.with(this)
                        .load(urls[index])
                        .holder(R.mipmap.ic_launcher)
                        .fade()
                        .into(imageView)
            }
            println(System.currentTimeMillis())

        }

        btn_clear.click {
            arrayOf(iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9).forEachIndexed { index, imageView ->
                imageView.setImageBitmap(null)
            }
        }

    }

}
