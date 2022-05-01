package com.rivaldo.screeningtestsuitmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rivaldo.screeningtestsuitmedia.R
import com.rivaldo.screeningtestsuitmedia.adapter.GuestAdapter
import com.rivaldo.screeningtestsuitmedia.api.RetrofitClient
import com.rivaldo.screeningtestsuitmedia.databinding.ActivityGuestBinding
import com.rivaldo.screeningtestsuitmedia.model.Guest
import com.rivaldo.screeningtestsuitmedia.model.GuestResponse
import com.rivaldo.screeningtestsuitmedia.utils.CommonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class GuestActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter : GuestAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var binding: ActivityGuestBinding
    private var page = 1
    private var totalPage = 1
    private var isLoading = false
    private var selectedGuest : String? = null
    private var loading = MutableLiveData<Boolean>()
    private var swipeLoad = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGuestBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        initActionBar()
        loadObserver()
        layoutManager = GridLayoutManager(this, 2)
        binding.swipeRefresh.setOnRefreshListener(this)
        setupRecyclerView()
        getGuest(false)
        binding.recyclerViewGuest.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if (loading.value?.not()!! && page < totalPage){
                    if (visibleItemCount + pastVisibleItem >= total){
                        page++
                        getGuest(false)
                    }
                }

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun initActionBar(){
        var actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Guest"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun sendIntentToPreviousActivity(){
        val intent = Intent()
        if (selectedGuest != null){
            intent.putExtra(CommonUtil.GUEST_BUTTON, selectedGuest)
            setResult(RESULT_OK, intent)
        } else {
            setResult(RESULT_CANCELED, intent)
        }
    }

    override fun onBackPressed() {
        sendIntentToPreviousActivity()
        super.onBackPressed()
    }

    private fun loadObserver() {
        loading.observe(this, androidx.lifecycle.Observer { isLoading ->
            if (isLoading) {
                binding.progressBarGuest.visibility = View.VISIBLE
            } else {
                binding.progressBarGuest.visibility = View.GONE
            }
        })
        swipeLoad.observe(this, androidx.lifecycle.Observer { isLoading ->
            if (isLoading) {
                binding.swipeRefresh.isRefreshing = true
            } else {
                binding.swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun setupRecyclerView(){
        with(binding.recyclerViewGuest){
            setHasFixedSize(true)
            layoutManager = this@GuestActivity.layoutManager
            this@GuestActivity.adapter = GuestAdapter({ guest ->
                adapterOnClick(guest)
            })
            adapter = this@GuestActivity.adapter
        }
    }

    private fun adapterOnClick(guest : Guest){
        selectedGuest = guest.firstName+" "+guest.lastName
    }

    private fun getGuest(isOnRefresh: Boolean){
        if (!isOnRefresh) loading.value = true
        else swipeLoad.value = true
        val parameters = HashMap<String, String>()
        parameters["page"] = page.toString()
        parameters["per_page"] = 10.toString()
        RetrofitClient.instance.getUsers(parameters).enqueue(object : Callback<GuestResponse> {
            override fun onResponse(call: Call<GuestResponse>, response: Response<GuestResponse>) {
                totalPage = response.body()?.totalPage!!
                val listResponse = response.body()?.data
                if (listResponse != null){
                        adapter.addList(listResponse)
                }
                if  (page == totalPage){
                    binding.progressBarGuest.visibility = View.GONE
                } else {
                    binding.progressBarGuest.visibility = View.INVISIBLE
                }
                loading.value = false
                swipeLoad.value = false
            }

            override fun onFailure(call: Call<GuestResponse>, t: Throwable) {
                Toast.makeText(this@GuestActivity, "Failed to get data "+ t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        getGuest(true)
    }
}