package hzh.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PullTofresh extends ListView implements OnScrollListener {

	private int firstVisibleItem;
	private View footerView;
	private int footHeight;
	private int headerHeight;
	private View headerView;

	private Enum<pullState> state;
	private boolean isBottom;

	private enum pullState {
		PULL_TO_REFRESH, RELEASE_REFRESH, REDRESHING
	}

	public PullTofresh(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		initHeader();
		initFooter();

		this.setOnScrollListener(this);
	}

	private void initHeader() {

		headerView = View.inflate(getContext(), R.layout.header, null);
		headerView.measure(0, 0);
		headerHeight = headerView.getMeasuredHeight();

		this.addHeaderView(headerView);

		db = (DashBoard) headerView.findViewById(R.id.db);

		fl = (FrameLayout) headerView.findViewById(R.id.fl);
		needle = (Needle) headerView.findViewById(R.id.needle);
		finishLoading(true, true);

	}

	/**
	 * 载入数据完成
	 * 
	 * @param isSuccessLoading
	 * @param isHeaderRequestData
	 */
	public void finishLoading(boolean isSuccessLoading,
			boolean isHeaderRequestData) {
		if (isHeaderRequestData) {

			needle.clearAnimation();
			db.setVisibility(View.INVISIBLE);
			fl.setVisibility(View.GONE);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
					0);
			db.setLayoutParams(params);
			headerView.setPadding(0, 0, 0, 0);
			db.setProgress(0);
			state = pullState.PULL_TO_REFRESH;
		} else {
			isBottom = false;
			footerView.setPadding(0, -footHeight, 0, 0);

		}
	}

	private void initFooter() {
		footerView = View.inflate(getContext(), R.layout.footer, null);
		footerView.measure(0, 0);
		footHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footHeight, 0, 0);
		this.addFooterView(footerView);
	}

	public void setProcess(int i) {
		db.setProgress(i);
	}

	OnRefreshListener listener;

	public void setListener(OnRefreshListener listener) {
		this.listener = listener;
	}

	public interface OnRefreshListener {
		void onRefresh();

		void onLoadingMore();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (this.getLastVisiblePosition() == this.getCount() - 1) {
				if (!isBottom) {
					isBottom = true;
					footerView.setPadding(0, 0, 0, 0);

					this.setSelection(this.getCount());
					if (listener != null) {
						listener.onLoadingMore();
					}
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}

	private boolean isRecord;
	private int downY;
	private DashBoard db;
	private boolean isDashBoard_Out;
	private FrameLayout fl;
	private Needle needle;

	/**
	 * True if the event was handled, false otherwise.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!isRecord && firstVisibleItem == 0) {
				downY = (int) ev.getY();
				isRecord = true;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) ev.getY();
			if (!isRecord && firstVisibleItem == 0) {
				downY = moveY;
				isRecord = true;
			}

			if (isRecord) {
				db.setVisibility(View.VISIBLE);
				int dY = moveY - downY;

				isDashBoard_Out = (dY > 80) ? true : false;

				if (!isDashBoard_Out) {

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							dY >= 0 ? dY : 0, dY >= 0 ? dY : 0);
					db.setLayoutParams(params);
					runSetProgress(dY);
				} else {
					headerView.setPadding(0, dY - 80, 0, 0);
				}

				if (dY >= 0 && state == pullState.PULL_TO_REFRESH) {

					state = pullState.RELEASE_REFRESH;

				} else if (dY < 0 && state == pullState.RELEASE_REFRESH) {

					state = pullState.PULL_TO_REFRESH;

				}

				// 第一个条目可见 && 向下滑
				// 则处理事件
				if (firstVisibleItem == 0 && dY > 0) {
					return true;
				}

			}

			break;
		case MotionEvent.ACTION_UP:
			isRecord = false;
			if (state == pullState.RELEASE_REFRESH) {
				state = pullState.REDRESHING;

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						80, 80);

				db.setVisibility(View.GONE);
				fl.setVisibility(View.VISIBLE);
				needle.rotate();

				db.setLayoutParams(params);
				headerView.setPadding(0, 0, 0, 0);

				if (listener != null) {

					listener.onRefresh();

				}
			} else if (state == pullState.PULL_TO_REFRESH) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						0, 0);
				db.setLayoutParams(params);
				headerView.setPadding(0, 0, 0, 0);
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void runSetProgress(int dY) {
		db.setProgress(dY * 100 / 80);
	}

}
