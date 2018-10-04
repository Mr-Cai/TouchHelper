package touch.helper.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import touch.helper.R
import touch.helper.adapter.SwipeAdapter
import touch.helper.modle.ImageBean

open class SwipeActivity : AppCompatActivity() {
    //列表视图
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSwipeAdapter: SwipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
        mRecyclerView = findViewById(R.id.activity_drag_recycler)
        val datas = ArrayList<Any>()
        datas.add(ImageBean(R.drawable.a))
        datas.add(ImageBean(R.drawable.b))
        datas.add(ImageBean(R.drawable.c))
        datas.add(ImageBean(R.drawable.d))
        datas.add(ImageBean(R.drawable.e))
        mSwipeAdapter = SwipeAdapter(datas)
        mSwipeAdapter.attachToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this.baseContext)
        mRecyclerView.adapter = mSwipeAdapter
    }

}