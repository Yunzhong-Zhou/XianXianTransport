package com.transport.xianxian.view.photochooser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.transport.xianxian.R;

import java.io.File;

import androidx.fragment.app.Fragment;

/**
 *
 * Created by chaochen on 2014-9-7.
 */
public class ImagePagerFragment extends Fragment {

	// @ViewById
	// DonutProgress circleLoading;
	public static final String URI_ARG = "uri";
	ImageView imageLoadFail;
	ViewGroup rootLayout;
	View image;

	public void setData(String uriString) {
		uri = uriString;
	}

	File mFile;
	String uri;

	// @FragmentArg
	// String fileId;
	//
	// @FragmentArg
	// int mProjectObjectId;

	// public void setData(String fileId, int mProjectObjectId) {
	// this.fileId = fileId;
	// this.mProjectObjectId = mProjectObjectId;
	// }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args_ = getArguments();
		uri = args_.getString(URI_ARG);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.activity_image_pager_item, null, false);
		imageLoadFail = (ImageView) view.findViewById(R.id.imageLoadFail);
		rootLayout = (ViewGroup) view.findViewById(R.id.rootLayout);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		showPhoto();
	}
	
	public static DisplayImageOptions optionsImage = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.mipmap.image_error)
			.showImageOnFail(R.mipmap.image_error)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.resetViewBeforeLoading(true).cacheInMemory(false)
			.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
			.build();

	// void init() {
	// circleLoading.setVisibility(View.INVISIBLE);
	// if (uri == null) {
	// parentActivity = (AttachmentsPicDetailActivity) getActivity();
	// if (parentActivity != null) {
	// // 在AttachmentsPicDetailActivity中存放了缓存下来的结果
	// picCache = parentActivity.getPicCache();
	// if (picCache.containsKey(fileId)) {
	// AttachmentFileObject mFileObject = picCache.get(fileId);
	// uri = mFileObject.preview;
	// showPhoto();
	// } else {
	// // 如果之前没有缓存过，那么获取并在得到结果后存入
	// URL_FILES = String.format(URL_FILES_BASE, mProjectObjectId,
	// fileId);
	// getNetwork(URL_FILES, URL_FILES);
	// }
	// }
	// } else {
	// showPhoto();
	// }
	// }

	@Override
	public void onDestroyView() {
		// if (image != null) {
		// if (image instanceof GifImageView) {
		// ((GifImageView) image).setImageURI(null);
		// } else if (image instanceof PhotoView) {
		// try {
		// ((BitmapDrawable) ((PhotoView)
		// image).getDrawable()).getBitmap().recycle();
		// } catch (Exception e) {
		// Global.errorLog(e);
		// }
		// }
		// }

		super.onDestroyView();
	}

	private void showPhoto() {
		if (!isAdded()) {
			return;
		}

//		ImageSize size = new ImageSize(getActivity().getResources()
//				.getDisplayMetrics().widthPixels, getActivity().getResources()
//				.getDisplayMetrics().heightPixels);
	
		ImageLoader.getInstance().displayImage(uri, imageLoadFail, optionsImage);

//		mFile = FileUtil.getDestinationInExternalPublicDir(
//				getFileDownloadPath(), uri.replaceAll(".*/(.*?)", "$1"));
	}

//	private final View.OnClickListener onClickImageClose = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			getActivity().onBackPressed();
//		}
//	};
//
//	private final PhotoViewAttacher.OnPhotoTapListener onPhotoTapClose = new PhotoViewAttacher.OnPhotoTapListener() {
//		@Override
//		public void onPhotoTap(View view, float v, float v2) {
//			getActivity().onBackPressed();
//		}
//	};

//	@Override
//	public void parseJson(int code, JSONObject response, String tag, int pos,
//			Object data) throws JSONException {
//		if (tag.equals(URL_FILES)) {
//			if (code == 0) {
//				JSONObject file = response.getJSONObject("data").getJSONObject(
//						"file");
//				AttachmentFileObject mFileObject = new AttachmentFileObject(
//						file);
//				if (picCache != null) {
//					picCache.put(mFileObject.file_id, mFileObject);
//					parentActivity.setAttachmentFileObject(mFileObject);
//				}
//				uri = mFileObject.preview;
//				showPhoto();
//			} else {
//				showErrorMsg(code, response);
//			}
//		}
//	}

//	private AsyncHttpClient client;

//	@Override
//	public void onDestroy() {
//		if (client != null) {
//			client.cancelRequests(getActivity(), true);
//			client = null;
//		}
//
//		super.onDestroy();
//	}

//	public String getFileDownloadPath() {
//		String path;
//		String defaultPath = Environment.DIRECTORY_DOWNLOADS + File.separator
//				+ FileUtil.DOWNLOAD_FOLDER;
//		SharedPreferences share = getActivity().getSharedPreferences(
//				FileUtil.DOWNLOAD_SETTING, Context.MODE_PRIVATE);
//		if (share.contains(FileUtil.DOWNLOAD_PATH)) {
//			path = share.getString(FileUtil.DOWNLOAD_PATH,
//					Environment.DIRECTORY_DOWNLOADS + File.separator
//							+ FileUtil.DOWNLOAD_FOLDER);
//		} else {
//			path = defaultPath;
//		}
//		return path;
//	}
}
