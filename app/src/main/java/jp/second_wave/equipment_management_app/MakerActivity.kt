package jp.second_wave.equipment_management_app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.second_wave.equipment_management_app.adapter.MakerListAdapter
import jp.second_wave.equipment_management_app.database.entitiy.Maker
import jp.second_wave.equipment_management_app.database.view_model.MakerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MakerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makers)

        setMakerList()

        val button = findViewById<ImageButton>(R.id.to_create_maker_page)
        button.setOnClickListener { setToCreateMakerPage() }
    }

    // 一覧データをセットする
    private fun setMakerList() {
        val makerViewModel: MakerViewModel by viewModels()

        GlobalScope.launch(Dispatchers.Main) {
            val makers = makerViewModel.getAll()
            val adapter = MakerListAdapter(this@MakerActivity, makers, supportFragmentManager)
            val listView: ListView = findViewById(R.id.maker_list)
            listView.adapter = adapter
            listView.onItemClickListener = ListItemClickListener()
        }
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val maker = parent.getItemAtPosition(position) as Maker

            setToUpdateMakerPage(maker)
        }
    }

    // 一覧画面へ切り替える
    private fun setToMakerListPage() {
        setContentView(R.layout.activity_makers)
        title = getString(R.string.menu_maker_list)

        setMakerList()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val button = findViewById<ImageButton>(R.id.to_create_maker_page)
        button.setOnClickListener { setToCreateMakerPage() }
    }

    // 更新画面へ切り替える
    private fun setToUpdateMakerPage(maker: Maker) {
        setContentView(R.layout.activity_maker)
        title = getString(R.string.maker_update)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val makerName = findViewById<View>(R.id.maker_name) as EditText
        makerName.setText(maker.makerName)

        val button: Button = findViewById<View>(R.id.create_maker_button) as Button
        button.setOnClickListener { updateMaker(maker) }
    }

    // 新規登録画面へ切り替える
    private fun setToCreateMakerPage() {
        setContentView(R.layout.activity_maker)
        title = getString(R.string.maker_create)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val button: Button = findViewById<View>(R.id.create_maker_button) as Button
        button.setOnClickListener { createMaker() }
    }

    // 削除アクション
    private fun deleteMaker(maker: Maker) {
        // TODO: 削除処理
        val makerViewModel: MakerViewModel by viewModels()
        GlobalScope.launch(Dispatchers.IO) {
            makerViewModel.delete(maker)
        }
        val toast: Toast = Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT)
        toast.show()

        setToMakerListPage()
    }

    // 更新アクション
    private fun updateMaker(maker: Maker) {
        val makerViewModel: MakerViewModel by viewModels()
        val makerName = findViewById<View>(R.id.maker_name) as EditText

        if (makerName.text.toString().isEmpty()) {
            makerName.error = "メーカー名を入力してください"
        } else {
            maker.makerName = makerName.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                makerViewModel.update(maker)
            }
            val toast: Toast = Toast.makeText(this, "更新しました", Toast.LENGTH_SHORT)
            toast.show()

            setToMakerListPage()
        }
    }

    // 登録アクション
    private fun createMaker() {
        val makerViewModel: MakerViewModel by viewModels()
        val makerName = findViewById<View>(R.id.maker_name) as EditText

        if (makerName.text.toString().isEmpty()) {
            makerName.error = "メーカー名を入力してください"
        } else {
            val maker = Maker(
                0,
                makerName.text.toString()
            )
            GlobalScope.launch(Dispatchers.IO) {
                makerViewModel.insert(maker)
            }

            val toast: Toast = Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT)
            toast.show()

            setToMakerListPage()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        finish()
        setToMakerListPage()
        return false // super.onSupportNavigateUp()
    }
}
