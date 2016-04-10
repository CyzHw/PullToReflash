package hzh.myapplication;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {

	private PullTofresh pullTofresh;
	private List<String> lists;
	private boolean isHeaderRequestData;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
	}

	private void init() {
		initData();

		pullTofresh = (PullTofresh) findViewById(R.id.ptr);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lists);
		pullTofresh.setAdapter(adapter);
		pullTofresh.setListener(new PullTofresh.OnRefreshListener() {

			@Override
			public void onRefresh() {
				isHeaderRequestData = true;

				getData();
			}

			@Override
			public void onLoadingMore() {
				isHeaderRequestData = false;
				getData();
			}
		});
	}

	protected void getData() {
		new MyAsyncTask().execute();

	}

	// AsyncTask 异步任务
	// 三个参数
	// Params:传入的参数的类型
	// Progress:进度的参数的类型
	// Result:结果的参数的类型
	// 四个方法
	class MyAsyncTask extends AsyncTask<Boolean, Void, String> {


		// 做准备工作的回调,运行在主线程,在doInBackground方法执行之前执行
		// Runs on the UI thread before doInBackground.
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		// Override this method to perform a computation on a background thread.
		// The specified parameters are the parameters passed to execute by the
		// caller of this task. This method can call publishProgress to publish
		// updates on the UI thread.
		@Override
		protected String doInBackground(Boolean... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String result;
			if (isHeaderRequestData) {
				result = "new data";

			} else {
				result = "new data";
			}
			return result;
		}

		// 进度更新的回调,运行在主线程,在publishProgress方法执行之后执行
		// Runs on the UI thread after publishProgress is invoked. The specified
		// values are the values passed to publishProgress.
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		// 结果处理的回调,运行在主线程,在doInBackground方法执行完成后执行,doInBackground方法返回值传入到此方法参数中
		// Runs on the UI thread after doInBackground. The specified result is
		// the value returned by doInBackground.
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (isHeaderRequestData) {
				lists.add(0, result);
			} else {
				lists.add(result);
			}

			adapter.notifyDataSetChanged();
			pullTofresh.finishLoading(true, isHeaderRequestData);
		}
	}

	private void initData() {
		lists = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			lists.add("条目" + i);
		}
	}

}
