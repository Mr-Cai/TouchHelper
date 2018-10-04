package touch.helper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_kotlin.*
import touch.helper.view.DragActivity
import touch.helper.view.SwipeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        gridLayout!!.setOnClickListener {
            startActivity(Intent(baseContext, DragActivity::class.java))
        }
        listLayout!!.setOnClickListener {
            startActivity(Intent(baseContext, SwipeActivity::class.java))
        }
    }
}
