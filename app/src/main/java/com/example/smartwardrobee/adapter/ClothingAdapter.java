package com.example.smartwardrobee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import com.example.smartwardrobee.R;
import com.example.smartwardrobee.model.ClothingItem;
import com.example.smartwardrobee.model.Season;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * 衣物列表适配器
 */
public class ClothingAdapter extends ListAdapter<ClothingItem, ClothingAdapter.ClothingViewHolder> {
    
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;
    private OnWearClickListener onWearClickListener;
    private OnMoreOptionsClickListener onMoreOptionsClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(ClothingItem item);
    }
    
    public interface OnFavoriteClickListener {
        void onFavoriteClick(ClothingItem item, boolean isFavorite);
    }
    
    public interface OnWearClickListener {
        void onWearClick(ClothingItem item);
    }

    public interface OnMoreOptionsClickListener {
        void onMoreOptionsClick(ClothingItem item, View anchorView);
    }
    
    public ClothingAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }
    
    private static final DiffUtil.ItemCallback<ClothingItem> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<ClothingItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull ClothingItem oldItem, @NonNull ClothingItem newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ClothingItem oldItem, @NonNull ClothingItem newItem) {
                // 安全地比较可能为null的字段
                String oldName = oldItem.getName() != null ? oldItem.getName() : "";
                String newName = newItem.getName() != null ? newItem.getName() : "";
                String oldColor = oldItem.getColor() != null ? oldItem.getColor() : "";
                String newColor = newItem.getColor() != null ? newItem.getColor() : "";

                return oldName.equals(newName) &&
                       oldItem.getType() == newItem.getType() &&
                       oldColor.equals(newColor) &&
                       oldItem.isFavorite() == newItem.isFavorite() &&
                       oldItem.getWearCount() == newItem.getWearCount() &&
                       oldItem.getStatus() == newItem.getStatus();
            }
        };
    
    @NonNull
    @Override
    public ClothingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clothing, parent, false);
        return new ClothingViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ClothingViewHolder holder, int position) {
        ClothingItem item = getItem(position);
        holder.bind(item);
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.onFavoriteClickListener = listener;
    }
    
    public void setOnWearClickListener(OnWearClickListener listener) {
        this.onWearClickListener = listener;
    }

    public void setOnMoreOptionsClickListener(OnMoreOptionsClickListener listener) {
        this.onMoreOptionsClickListener = listener;
    }
    
    class ClothingViewHolder extends RecyclerView.ViewHolder {
        private ImageView clothingImage;
        private ImageView favoriteIcon;
        private TextView clothingName;
        private TextView clothingType;
        private TextView clothingBrandPrice;
        private View colorIndicator;
        private Chip statusChip;
        private TextView wearCount;
        private ImageView moreOptions;
        
        public ClothingViewHolder(@NonNull View itemView) {
            super(itemView);
            
            clothingImage = itemView.findViewById(R.id.clothing_image);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
            clothingName = itemView.findViewById(R.id.clothing_name);
            clothingType = itemView.findViewById(R.id.clothing_type);
            clothingBrandPrice = itemView.findViewById(R.id.clothing_brand_price);
            colorIndicator = itemView.findViewById(R.id.color_indicator);
            statusChip = itemView.findViewById(R.id.status_chip);
            wearCount = itemView.findViewById(R.id.wear_count);
            moreOptions = itemView.findViewById(R.id.more_options);
            
            // 设置点击事件
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(getItem(position));
                    }
                }
            });
            
            favoriteIcon.setOnClickListener(v -> {
                if (onFavoriteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ClothingItem item = getItem(position);
                        onFavoriteClickListener.onFavoriteClick(item, !item.isFavorite());
                    }
                }
            });
            
            statusChip.setOnClickListener(v -> {
                if (onWearClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onWearClickListener.onWearClick(getItem(position));
                    }
                }
            });

            moreOptions.setOnClickListener(v -> {
                if (onMoreOptionsClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onMoreOptionsClickListener.onMoreOptionsClick(getItem(position), v);
                    }
                }
            });
        }
        
        public void bind(ClothingItem item) {
            if (item == null) return;

            // 设置衣物名称
            clothingName.setText(item.getName() != null ? item.getName() : "未命名");

            // 设置类型和季节信息
            StringBuilder typeInfo = new StringBuilder();
            if (item.getType() != null) {
                typeInfo.append(item.getType().getDisplayName());
            } else {
                typeInfo.append("未分类");
            }

            if (item.getSeasons() != null && !item.getSeasons().isEmpty()) {
                typeInfo.append(" · ");
                for (int i = 0; i < item.getSeasons().size(); i++) {
                    if (i > 0) typeInfo.append("、");
                    Season season = item.getSeasons().get(i);
                    if (season != null) {
                        typeInfo.append(season.getDisplayName());
                    }
                }
            }
            clothingType.setText(typeInfo.toString());
            
            // 设置品牌和价格
            StringBuilder brandPrice = new StringBuilder();
            if (!TextUtils.isEmpty(item.getBrand())) {
                brandPrice.append(item.getBrand());
            }
            if (item.getPrice() > 0) {
                if (brandPrice.length() > 0) {
                    brandPrice.append(" · ");
                }
                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.CHINA);
                brandPrice.append(formatter.format(item.getPrice()));
            }
            
            if (brandPrice.length() > 0) {
                clothingBrandPrice.setText(brandPrice.toString());
                clothingBrandPrice.setVisibility(View.VISIBLE);
            } else {
                clothingBrandPrice.setVisibility(View.GONE);
            }
            
            // 设置收藏状态
            if (item.isFavorite()) {
                favoriteIcon.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
            }
            
            // 设置颜色指示器
            String color = item.getColor() != null ? item.getColor() : "灰色";
            int colorInt = getColorFromName(color);
            colorIndicator.setBackgroundColor(colorInt);

            // 设置状态标签
            if (item.getStatus() != null) {
                statusChip.setText(item.getStatus().getDisplayName());
                statusChip.setChipBackgroundColorResource(getStatusColor(item.getStatus()));
            } else {
                statusChip.setText("可穿");
                statusChip.setChipBackgroundColorResource(R.color.status_available);
            }
            
            // 设置穿着次数
            if (item.getWearCount() > 0) {
                wearCount.setText(context.getString(R.string.wear_count_format, item.getWearCount()));
                wearCount.setVisibility(View.VISIBLE);
            } else {
                wearCount.setVisibility(View.GONE);
            }
            
            // 加载图片
            if (!TextUtils.isEmpty(item.getImagePath())) {
                Glide.with(context)
                    .load(item.getImagePath())
                    .placeholder(R.drawable.ic_clothing_placeholder)
                    .error(R.drawable.ic_clothing_placeholder)
                    .centerCrop()
                    .into(clothingImage);
            } else {
                clothingImage.setImageResource(R.drawable.ic_clothing_placeholder);
            }
        }
        
        private int getColorFromName(String colorName) {
            if (TextUtils.isEmpty(colorName)) {
                return Color.GRAY;
            }
            
            switch (colorName) {
                case "白色": return Color.WHITE;
                case "黑色": return Color.BLACK;
                case "红色": return Color.RED;
                case "蓝色": return Color.BLUE;
                case "绿色": return Color.GREEN;
                case "黄色": return Color.YELLOW;
                case "橙色": return Color.parseColor("#FF9800");
                case "紫色": return Color.parseColor("#9C27B0");
                case "粉色": return Color.parseColor("#E91E63");
                case "棕色": return Color.parseColor("#795548");
                case "灰色": return Color.GRAY;
                case "深蓝色": return Color.parseColor("#1A237E");
                default: return Color.GRAY;
            }
        }
        
        private int getStatusColor(com.example.smartwardrobee.model.ClothingStatus status) {
            switch (status) {
                case AVAILABLE: return R.color.status_available;
                case DIRTY: return R.color.status_dirty;
                case WASHING: return R.color.status_washing;
                case DRYING: return R.color.status_drying;
                case NEED_REPAIR: return R.color.status_need_repair;
                default: return R.color.status_available;
            }
        }
    }
}
