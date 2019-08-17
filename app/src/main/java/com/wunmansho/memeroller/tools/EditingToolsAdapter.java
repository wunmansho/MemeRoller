package com.wunmansho.memeroller.tools;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wunmansho.memeroller.EditImageActivity;
import com.wunmansho.memeroller.R;
import com.wunmansho.memeroller.anim.BullHornBounceInterpolator;
import com.wunmansho.util.utils;

import java.util.ArrayList;
import java.util.List;

import static com.wunmansho.memeroller.tools.ToolType.MIC;
import static com.wunmansho.memeroller.tools.ToolType.SHARE;
import static com.wunmansho.object.ApplicationContextProvider.getContext;



/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */
public class EditingToolsAdapter extends RecyclerView.Adapter<EditingToolsAdapter.ViewHolder> {

    private List<ToolModel> mToolList = new ArrayList<>();
    private OnItemSelected mOnItemSelected;
    private boolean micOn;
    private Context context;
    private RotateAnimation rotateAnimation;
    private Resources res;
     private String mMic;
    private String mText;
    private String mEmoji;
    private String mSticker;
    private String mUndo;
    private String mRedo;
    private String mSave;
    private String mShare;
    private String mEraser;
    private String mBrush;
    private String mFilter;
    public EditingToolsAdapter(OnItemSelected onItemSelected) {
        context = getContext();
        res = context.getResources();
        mMic = res.getString(R.string.label_mic);
        mText = res.getString(R.string.label_text);
        mEmoji = res.getString(R.string.label_emoji);
        mSticker = res.getString(R.string.label_sticker);
        mUndo = res.getString(R.string.label_undo);
        mRedo = res.getString(R.string.label_redo);
        mSave = res.getString(R.string.label_save);

        mShare = res.getString(R.string.label_share);
        mEraser = res.getString(R.string.label_eraser);
        mBrush = res.getString(R.string.label_brush);
        mFilter = res.getString(R.string.label_filter);

        mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel(mMic, R.drawable.ic_mic_off_white_36dp, MIC));
        mToolList.add(new ToolModel(mText, R.drawable.ic_text, ToolType.TEXT));
        mToolList.add(new ToolModel(mEmoji, R.drawable.ic_insert_emoticon, ToolType.EMOJI));
        mToolList.add(new ToolModel(mSticker, R.drawable.ic_sticker, ToolType.STICKER));
        mToolList.add(new ToolModel(mUndo, R.drawable.ic_undo, ToolType.UNDO));
        mToolList.add(new ToolModel(mRedo, R.drawable.ic_redo, ToolType.REDO));
        mToolList.add(new ToolModel(mSave, R.drawable.ic_save, ToolType.SAVE));
        mToolList.add(new ToolModel(mShare, R.drawable.ic_share_white_36dp, ToolType.SHARE));
        mToolList.add(new ToolModel(mEraser, R.drawable.ic_eraser, ToolType.ERASER));
        mToolList.add(new ToolModel(mBrush, R.drawable.ic_brush, ToolType.BRUSH));
        mToolList.add(new ToolModel(mFilter, R.drawable.ic_photo_filter, ToolType.FILTER));

    }

    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);
    }

    class ToolModel {
        private String mToolName;
        private int mToolIcon;
        public ToolType mToolType;

        ToolModel(String toolName, int toolIcon, ToolType toolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mToolType = toolType;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_editing_tools, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ToolModel item = mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
        holder.imgToolIcon.setTag(item.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View itemView) {
            super(itemView);

            imgToolIcon = itemView.findViewById(R.id.imgToolIcon);
            txtTool = itemView.findViewById(R.id.txtTool);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  Integer resource = (Integer) imgToolIcon.getTag();
                  if(resource == R.drawable.ic_undo) {
                      rotateAnimation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                  } else {
                      rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                  }
                    rotateAnimation.setRepeatCount(1);
                    rotateAnimation.setRepeatMode(Animation.RELATIVE_TO_SELF);
                    rotateAnimation.setDuration(100);
                    imgToolIcon.startAnimation(rotateAnimation);

                    mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mToolType);

                                  }
            });
        }
    }
}
