package com.example.smartwardrobee;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import com.example.smartwardrobee.adapter.ClothingAdapter;
import com.example.smartwardrobee.model.*;
import com.example.smartwardrobee.viewmodel.ClothingViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ClothingViewModel clothingViewModel;
    private ClothingAdapter clothingAdapter;

    // UI组件
    private MaterialToolbar toolbar;
    private TextInputEditText searchEditText;
    private ChipGroup seasonChipGroup, typeChipGroup;
    private RecyclerView clothingRecyclerView;
    private LinearLayout emptyStateLayout;
    private FloatingActionButton fabAddClothing;

    // 当前筛选条件
    private Season currentSeason = null;
    private ClothingCategory currentCategory = null;
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel();
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupSearchAndFilters();
        setupFab();
        observeData();
    }

    private void initViewModel() {
        clothingViewModel = new ViewModelProvider(this).get(ClothingViewModel.class);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        searchEditText = findViewById(R.id.search_edit_text);
        seasonChipGroup = findViewById(R.id.season_chip_group);
        typeChipGroup = findViewById(R.id.type_chip_group);
        clothingRecyclerView = findViewById(R.id.clothing_recycler_view);
        emptyStateLayout = findViewById(R.id.empty_state_layout);
        fabAddClothing = findViewById(R.id.fab_add_clothing);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        try {
            clothingAdapter = new ClothingAdapter(this);
            clothingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            clothingRecyclerView.setAdapter(clothingAdapter);

            // 设置适配器回调
            clothingAdapter.setOnItemClickListener(item -> {
                // TODO: 打开衣物详情页面
            });

            clothingAdapter.setOnFavoriteClickListener((item, isFavorite) -> {
                if (item != null) {
                    clothingViewModel.updateFavoriteStatus(item.getId(), isFavorite);
                }
            });

            clothingAdapter.setOnWearClickListener(item -> {
                if (item != null) {
                    clothingViewModel.incrementWearCount(item.getId());
                    Toast.makeText(this, "已标记为穿过", Toast.LENGTH_SHORT).show();
                }
            });

            clothingAdapter.setOnMoreOptionsClickListener(this::showClothingOptionsMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearchAndFilters() {
        // 搜索功能
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 季节筛选
        seasonChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentSeason = null;
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chip_all_seasons) {
                    currentSeason = null;
                } else if (checkedId == R.id.chip_spring) {
                    currentSeason = Season.SPRING;
                } else if (checkedId == R.id.chip_summer) {
                    currentSeason = Season.SUMMER;
                } else if (checkedId == R.id.chip_autumn) {
                    currentSeason = Season.AUTUMN;
                } else if (checkedId == R.id.chip_winter) {
                    currentSeason = Season.WINTER;
                }
            }
            applyFilters();
        });

        // 类型筛选
        typeChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentCategory = null;
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chip_all_types) {
                    currentCategory = null;
                } else if (checkedId == R.id.chip_top) {
                    currentCategory = ClothingCategory.TOP;
                } else if (checkedId == R.id.chip_bottom) {
                    currentCategory = ClothingCategory.BOTTOM;
                } else if (checkedId == R.id.chip_shoes) {
                    currentCategory = ClothingCategory.SHOES;
                } else if (checkedId == R.id.chip_accessories) {
                    currentCategory = ClothingCategory.ACCESSORIES;
                }
            }
            applyFilters();
        });
    }

    private void setupFab() {
        fabAddClothing.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddClothingActivity.class);
            startActivity(intent);
        });
    }

    private void observeData() {
        clothingViewModel.getAllClothing().observe(this, clothingItems -> {
            Log.d(TAG, "Observer triggered with " + (clothingItems != null ? clothingItems.size() : 0) + " items");
            if (clothingItems != null) {
                updateClothingList(clothingItems);
            }
        });
    }

    private void applyFilters() {
        // 移除之前的观察者，避免内存泄漏
        if (!currentSearchQuery.isEmpty()) {
            clothingViewModel.searchClothing(currentSearchQuery).removeObservers(this);
            clothingViewModel.searchClothing(currentSearchQuery).observe(this, clothingItems -> {
                if (clothingItems != null) {
                    updateClothingList(clothingItems);
                }
            });
        } else if (currentSeason != null) {
            clothingViewModel.getClothingBySeason(currentSeason).removeObservers(this);
            clothingViewModel.getClothingBySeason(currentSeason).observe(this, clothingItems -> {
                if (clothingItems != null) {
                    updateClothingList(clothingItems);
                }
            });
        } else {
            // 使用主要的观察者，不需要重新设置
            clothingViewModel.getAllClothing().removeObservers(this);
            clothingViewModel.getAllClothing().observe(this, clothingItems -> {
                if (clothingItems != null) {
                    updateClothingList(clothingItems);
                }
            });
        }
    }

    private void updateClothingList(List<ClothingItem> clothingItems) {
        Log.d(TAG, "updateClothingList called with " + (clothingItems != null ? clothingItems.size() : 0) + " items");

        try {
            if (clothingItems == null || clothingItems.isEmpty()) {
                clothingRecyclerView.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
                Log.d(TAG, "Showing empty state");
            } else {
                clothingRecyclerView.setVisibility(View.VISIBLE);
                emptyStateLayout.setVisibility(View.GONE);

                // 根据当前类型筛选进行过滤
                if (currentCategory != null) {
                    List<ClothingItem> filteredItems = new ArrayList<>();
                    for (ClothingItem item : clothingItems) {
                        if (item != null && item.getType() != null && item.getType().getCategory() == currentCategory) {
                            filteredItems.add(item);
                        }
                    }
                    clothingItems = filteredItems;
                    Log.d(TAG, "Filtered to " + clothingItems.size() + " items for category " + currentCategory);
                }

                clothingAdapter.submitList(clothingItems);
                Log.d(TAG, "Submitted list to adapter");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating clothing list", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_statistics) {
            // TODO: 打开统计页面
            return true;
        } else if (id == R.id.action_settings) {
            // TODO: 打开设置页面
            return true;
        } else if (id == R.id.action_about) {
            // TODO: 打开关于页面
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示衣物操作菜单
     */
    private void showClothingOptionsMenu(ClothingItem item, View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);
        popup.getMenuInflater().inflate(R.menu.clothing_item_menu, popup.getMenu());

        // 根据当前状态调整菜单项
        MenuItem favoriteItem = popup.getMenu().findItem(R.id.action_favorite);
        if (item.isFavorite()) {
            favoriteItem.setTitle("取消收藏");
            favoriteItem.setIcon(R.drawable.ic_favorite_filled);
        } else {
            favoriteItem.setTitle("收藏");
            favoriteItem.setIcon(R.drawable.ic_favorite_border);
        }

        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.action_wear) {
                handleWearAction(item);
                return true;
            } else if (itemId == R.id.action_edit) {
                handleEditAction(item);
                return true;
            } else if (itemId == R.id.action_favorite) {
                handleFavoriteAction(item);
                return true;
            } else if (itemId == R.id.action_change_status) {
                handleChangeStatusAction(item);
                return true;
            } else if (itemId == R.id.action_delete) {
                handleDeleteAction(item);
                return true;
            }

            return false;
        });

        popup.show();
    }

    /**
     * 处理标记为已穿操作
     */
    private void handleWearAction(ClothingItem item) {
        clothingViewModel.incrementWearCount(item.getId());
        Toast.makeText(this, "已标记为穿过", Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理编辑操作
     */
    private void handleEditAction(ClothingItem item) {
        Intent intent = new Intent(this, AddClothingActivity.class);
        intent.putExtra("EDIT_MODE", true);
        intent.putExtra("CLOTHING_ID", item.getId());
        startActivity(intent);
    }

    /**
     * 处理收藏操作
     */
    private void handleFavoriteAction(ClothingItem item) {
        boolean newFavoriteStatus = !item.isFavorite();
        clothingViewModel.updateFavoriteStatus(item.getId(), newFavoriteStatus);

        String message = newFavoriteStatus ? "已添加到收藏" : "已取消收藏";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理更改状态操作
     */
    private void handleChangeStatusAction(ClothingItem item) {
        String[] statusOptions = {
            "可穿", "需洗", "洗涤中", "晾晒中", "需修补", "收纳中"
        };

        ClothingStatus[] statusValues = {
            ClothingStatus.AVAILABLE,
            ClothingStatus.DIRTY,
            ClothingStatus.WASHING,
            ClothingStatus.DRYING,
            ClothingStatus.NEED_REPAIR,
            ClothingStatus.STORED
        };

        int currentIndex = 0;
        for (int i = 0; i < statusValues.length; i++) {
            if (statusValues[i] == item.getStatus()) {
                currentIndex = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
            .setTitle("选择状态")
            .setSingleChoiceItems(statusOptions, currentIndex, (dialog, which) -> {
                clothingViewModel.updateStatus(item.getId(), statusValues[which]);
                Toast.makeText(this, "状态已更新为：" + statusOptions[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 处理删除操作
     */
    private void handleDeleteAction(ClothingItem item) {
        new AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除「" + item.getName() + "」吗？此操作无法撤销。")
            .setPositiveButton("删除", (dialog, which) -> {
                clothingViewModel.delete(item).thenRun(() -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                    });
                }).exceptionally(throwable -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                    });
                    return null;
                });
            })
            .setNegativeButton("取消", null)
            .show();
    }
}
