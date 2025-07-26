package com.permissions.chinky.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.permissions.chinky.R
import com.permissions.chinky.utility.Utility

class LifecycleInfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
        return inflater.inflate(R.layout.fragment_lifecycle_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onResume() {
        super.onResume()
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onPause() {
        super.onPause()
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onDetach() {
        super.onDetach()
        Utility.printLog(LifecycleInfoFragment::class.java, Thread.currentThread().stackTrace[2], "")
    }

    companion object {
        @JvmStatic
        fun newInstance() = LifecycleInfoFragment()
    }
}