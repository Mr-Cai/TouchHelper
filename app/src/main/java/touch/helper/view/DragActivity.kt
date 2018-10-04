package touch.helper.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import touch.helper.R
import touch.helper.adapter.DragAdapter
import touch.helper.modle.ImageBean

open class DragActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    lateinit var dragAdapter: DragAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
        mRecyclerView = findViewById(R.id.activity_drag_recycler)
        val datas = ArrayList<Any>()
        datas.add("title1")
        datas.add(ImageBean(R.drawable.a))
        datas.add(ImageBean(R.drawable.a))
        datas.add(ImageBean(R.drawable.a))
        datas.add("title2")
        datas.add(ImageBean(R.drawable.b))
        datas.add(ImageBean(R.drawable.b))
        datas.add(ImageBean(R.drawable.b))
        datas.add(ImageBean(R.drawable.b))
        datas.add("title3")
        datas.add(ImageBean(R.drawable.c))
        datas.add(ImageBean(R.drawable.c))
        datas.add(ImageBean(R.drawable.c))
        datas.add(ImageBean(R.drawable.c))
        datas.add("title4")
        datas.add(ImageBean(R.drawable.d))
        datas.add(ImageBean(R.drawable.d))
        datas.add(ImageBean(R.drawable.d))
        datas.add(ImageBean(R.drawable.d))
        datas.add("title5")
        datas.add(ImageBean(R.drawable.e))
        datas.add(ImageBean(R.drawable.e))
        datas.add(ImageBean(R.drawable.e))
        datas.add(ImageBean(R.drawable.e))
        dragAdapter = DragAdapter(datas)
        dragAdapter.attachToRecyclerView(mRecyclerView)
        val gridLayoutManager = GridLayoutManager(this.baseContext, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = dragAdapter.getItemViewType(position)
                return when (viewType) {
                    DragAdapter.TYPE_TITLE -> {
                        3
                    }
                    else -> {
                        1
                    }
                }
            }
        }
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.adapter = dragAdapter
    }

}