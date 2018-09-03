package com.example.nitai.client_nitai;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.util.concurrent.BlockingQueue;

import static com.example.nitai.client_nitai.MainActivity.messagesList;

public class ObjectPopAsyncTask extends AsyncTask<BlockingQueue<Pair<String, WikiObject>>, Pair<String, WikiObject>, Void> {


    @SafeVarargs
    @Override
    protected final Void doInBackground(BlockingQueue<Pair<String, WikiObject>>... blockingQueues) {
        while (!isCancelled()) {
            try {
                Pair pair = MainActivity.wikiMapQueue.take();
                MainActivity.wikiMapQueue2.add(pair);
                Log.i("bubblePop", "object taken: : " + pair.first.toString());
                this.publishProgress(pair);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    protected void onPostExecute(Void a) {
    }

    @SafeVarargs
    @Override
    protected final void onProgressUpdate(Pair<String, WikiObject>... pairs) {
        messagesList.popBubble(pairs[0]);
    }


}
