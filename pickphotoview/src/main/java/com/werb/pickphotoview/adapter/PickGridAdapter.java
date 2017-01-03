package com.werb.pickphotoview.adapter;import android.content.Context;import android.graphics.drawable.Drawable;import android.net.Uri;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import android.widget.RelativeLayout;import android.widget.Toast;import com.bumptech.glide.Glide;import com.werb.pickphotoview.MyApp;import com.werb.pickphotoview.PickPhotoActivity;import com.werb.pickphotoview.util.PickConfig;import com.werb.pickphotoview.R;import com.werb.pickphotoview.util.PickUtils;import java.util.ArrayList;import java.util.List;/** * Created by wanbo on 2016/12/31. */public class PickGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {    private List<String> imagePaths;    private boolean isShowCamera;    private int spanCount;    private int maxSelectSize;    private List<String> selectPath;    private PickPhotoActivity context;    public PickGridAdapter(Context c, List<String> imagePaths, boolean isShowCamera, int spanCount, int maxSelectSize) {        this.context = (PickPhotoActivity) c;        this.imagePaths = imagePaths;        this.isShowCamera = isShowCamera;        this.spanCount = spanCount;        this.maxSelectSize = maxSelectSize;        selectPath = new ArrayList<>();    }    @Override    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {        return new GridImageViewHolder(LayoutInflater.from(MyApp.getApp()).inflate(R.layout.item_grid_layout, parent, false));    }    @Override    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {        if (position == PickConfig.CAMERA_TYPE) {        } else {            String path = imagePaths.get(position);            GridImageViewHolder gridImageViewHolder = (GridImageViewHolder) holder;            gridImageViewHolder.bindItem(path);        }    }    @Override    public int getItemViewType(int position) {        if (isShowCamera) {            return PickConfig.CAMERA_TYPE;        } else {            return position;        }    }    @Override    public int getItemCount() {        if (isShowCamera) {            return imagePaths.size() + 1;        } else {            return imagePaths.size();        }    }    public void updateData(List<String> paths) {        imagePaths = paths;        notifyDataSetChanged();    }    // ViewHolder    private class GridImageViewHolder extends RecyclerView.ViewHolder {        private ImageView gridImage, selectImage;        private int scaleSize;        GridImageViewHolder(View itemView) {            super(itemView);            gridImage = (ImageView) itemView.findViewById(R.id.iv_grid);            selectImage = (ImageView) itemView.findViewById(R.id.iv_select);            selectImage.setBackgroundDrawable(null);            selectImage.setBackgroundDrawable(MyApp.getApp().getResources().getDrawable(R.mipmap.ic_un_select));            selectImage.setTag(R.id.is_select, false);            int screenWidth = PickUtils.getWidthPixels();            int space = PickUtils.dp2px(PickConfig.ITEM_SPACE);            scaleSize = (screenWidth - (spanCount + 1) * space) / spanCount;            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) gridImage.getLayoutParams();            params.width = scaleSize;            params.height = scaleSize;        }        void bindItem(String path) {            Glide.with(MyApp.getApp()).load(Uri.parse("file://" + path)).thumbnail(0.3f).into(gridImage);            gridImage.setTag(R.id.image_path, path);            selectImage.setOnClickListener(v -> {                boolean isSelect = (boolean) selectImage.getTag(R.id.is_select);                if (isSelect) {                    selectImage.setBackgroundDrawable(null);                    selectImage.setBackgroundDrawable(MyApp.getApp().getResources().getDrawable(R.mipmap.ic_un_select));                    selectImage.setTag(R.id.is_select, false);                    selectPath.remove(path);                    context.updateSelectText(String.valueOf(selectPath.size()));                } else {                    if (selectPath.size() < maxSelectSize) {                        selectImage.setBackgroundDrawable(null);                        selectImage.setBackgroundDrawable(MyApp.getApp().getResources().getDrawable(R.mipmap.ic_select));                        selectImage.setTag(R.id.is_select, true);                        selectPath.add(path);                        context.updateSelectText(String.valueOf(selectPath.size()));                    } else {                        Toast.makeText(context, String.format(context.getString(R.string.photo_size_limit), String.valueOf(maxSelectSize)), Toast.LENGTH_SHORT).show();                    }                }            });        }    }}