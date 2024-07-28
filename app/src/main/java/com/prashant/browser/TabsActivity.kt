package com.prashant.browser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TabsActivity : AppCompatActivity() {

    private lateinit var tabsRecyclerView: RecyclerView
    private lateinit var tabsAdapter: TabsAdapter
    private val tabsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)

        tabsRecyclerView = findViewById(R.id.tabsRecyclerView)
        tabsRecyclerView.layoutManager = LinearLayoutManager(this)
        tabsAdapter = TabsAdapter(tabsList)
        tabsRecyclerView.adapter = tabsAdapter

        // Populate tabs list (you'll need to implement a way to track open tabs)
        tabsList.addAll(intent.getStringArrayListExtra("TABS_LIST") ?: emptyList())

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.newTabButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("NEW_TAB", true)
            startActivity(intent)
            finish()
        }
    }

    inner class TabsAdapter(private val tabs: List<String>) :
        RecyclerView.Adapter<TabsAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titleTextView: TextView = view.findViewById(R.id.tabTitleTextView)
            val closeButton: ImageButton = view.findViewById(R.id.closeTabButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tab_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.titleTextView.text = tabs[position]
            holder.itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("SELECTED_TAB", position)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            holder.closeButton.setOnClickListener {
                val intent = Intent()
                intent.putExtra("CLOSED_TAB", position)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        override fun getItemCount() = tabs.size
    }
}