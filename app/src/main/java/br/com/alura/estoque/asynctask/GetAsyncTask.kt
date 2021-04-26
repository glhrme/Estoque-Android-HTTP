package br.com.alura.estoque.asynctask

import android.os.AsyncTask
import android.util.Log

class GetAsyncTask(private val listener: GetAsyncTaskListener) : AsyncTask<Void?, Void?, Void>() {
    override fun doInBackground(vararg params: Void?): Void {
        return listener.toExecute()
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        listener.postExecute()
    }

    interface GetAsyncTaskListener {
        fun toExecute(): Void;
        fun postExecute();
    }
}