package com.example.smartwardrobee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import com.example.smartwardrobee.model.*;
import com.example.smartwardrobee.viewmodel.ClothingViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 添加衣物Activity
 */
public class AddClothingActivity extends AppCompatActivity {

    private ClothingViewModel clothingViewModel;

    // 编辑模式相关
    private boolean isEditMode = false;
    private long editingClothingId = -1;
    private ClothingItem editingItem;
    
    // UI组件
    private MaterialToolbar toolbar;
    private ImageView clothingImagePreview;
    private MaterialButton btnTakePhoto, btnSelectPhoto;
    private ExtendedFloatingActionButton fabSave;
    
    // 输入字段
    private TextInputEditText editClothingName, editBrand, editPrice, editDescription;
    private TextInputEditText editMaterial, editSize;
    private AutoCompleteTextView spinnerClothingType, spinnerColor;
    private ChipGroup seasonChipGroup;
    
    // 图片相关
    private String currentImagePath;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothing);

        // 检查是否为编辑模式
        checkEditMode();

        initViewModel();
        initViews();
        setupToolbar();
        setupSpinners();
        setupImagePickers();
        setupSaveButton();

        // 如果是编辑模式，加载数据
        if (isEditMode) {
            loadClothingData();
        }
    }

    private void checkEditMode() {
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("EDIT_MODE", false);
        editingClothingId = intent.getLongExtra("CLOTHING_ID", -1);
    }
    
    private void initViewModel() {
        clothingViewModel = new ViewModelProvider(this).get(ClothingViewModel.class);
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        clothingImagePreview = findViewById(R.id.clothing_image_preview);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnSelectPhoto = findViewById(R.id.btn_select_photo);
        fabSave = findViewById(R.id.fab_save);
        
        editClothingName = findViewById(R.id.edit_clothing_name);
        editBrand = findViewById(R.id.edit_brand);
        editPrice = findViewById(R.id.edit_price);
        editDescription = findViewById(R.id.edit_description);
        editMaterial = findViewById(R.id.edit_material);
        editSize = findViewById(R.id.edit_size);
        
        spinnerClothingType = findViewById(R.id.spinner_clothing_type);
        spinnerColor = findViewById(R.id.spinner_color);
        seasonChipGroup = findViewById(R.id.season_chip_group);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 根据模式设置标题
            if (isEditMode) {
                getSupportActionBar().setTitle("编辑衣物");
            } else {
                getSupportActionBar().setTitle("添加衣物");
            }
        }
    }
    
    private void setupSpinners() {
        // 设置衣物类型下拉列表
        List<String> clothingTypes = new ArrayList<>();
        for (ClothingType type : ClothingType.values()) {
            clothingTypes.add(type.getDisplayName());
        }
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_dropdown_item_1line, clothingTypes);
        spinnerClothingType.setAdapter(typeAdapter);
        
        // 设置颜色下拉列表
        String[] colors = {"白色", "黑色", "红色", "蓝色", "绿色", "黄色", 
                          "橙色", "紫色", "粉色", "棕色", "灰色", "深蓝色"};
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_dropdown_item_1line, Arrays.asList(colors));
        spinnerColor.setAdapter(colorAdapter);
    }
    
    private void setupImagePickers() {
        // 初始化Activity Result Launchers
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            clothingImagePreview.setImageBitmap(imageBitmap);
                            saveImageToFile(imageBitmap);
                        }
                    }
                }
            }
        );
        
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        clothingImagePreview.setImageURI(imageUri);
                        currentImagePath = imageUri.toString();
                    }
                }
            }
        );
        
        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "需要相机权限才能拍照", Toast.LENGTH_SHORT).show();
                }
            }
        );
        
        // 设置按钮点击事件
        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndTakePhoto());
        btnSelectPhoto.setOnClickListener(v -> openGallery());
    }
    
    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
    
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
    }
    
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }
    
    private void saveImageToFile(Bitmap bitmap) {
        try {
            // 确保目录存在
            File imageDir = new File(getFilesDir(), "images");
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }

            File imageFile = new File(imageDir, "clothing_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            currentImagePath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存图片失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            currentImagePath = null;
        }
    }
    
    private void setupSaveButton() {
        fabSave.setOnClickListener(v -> saveClothing());

        // 根据模式设置按钮文本
        if (isEditMode) {
            fabSave.setText("更新");
        } else {
            fabSave.setText("保存");
        }
    }

    /**
     * 加载衣物数据（编辑模式）
     */
    private void loadClothingData() {
        if (editingClothingId != -1) {
            clothingViewModel.getClothingById(editingClothingId).observe(this, item -> {
                if (item != null) {
                    editingItem = item;
                    populateFields(item);
                }
            });
        }
    }

    /**
     * 填充表单字段
     */
    private void populateFields(ClothingItem item) {
        // 填充基本信息
        editClothingName.setText(item.getName());
        editBrand.setText(item.getBrand());
        editDescription.setText(item.getDescription());
        editMaterial.setText(item.getMaterial());
        editSize.setText(item.getSize());

        if (item.getPrice() > 0) {
            editPrice.setText(String.valueOf(item.getPrice()));
        }

        // 设置衣物类型
        if (item.getType() != null) {
            spinnerClothingType.setText(item.getType().getDisplayName(), false);
        }

        // 设置颜色
        if (item.getColor() != null) {
            spinnerColor.setText(item.getColor(), false);
        }

        // 设置季节选择
        if (item.getSeasons() != null) {
            for (Season season : item.getSeasons()) {
                switch (season) {
                    case SPRING:
                        findViewById(R.id.chip_season_spring).performClick();
                        break;
                    case SUMMER:
                        findViewById(R.id.chip_season_summer).performClick();
                        break;
                    case AUTUMN:
                        findViewById(R.id.chip_season_autumn).performClick();
                        break;
                    case WINTER:
                        findViewById(R.id.chip_season_winter).performClick();
                        break;
                }
            }
        }

        // 加载图片
        if (item.getImagePath() != null) {
            currentImagePath = item.getImagePath();
            // 使用Glide加载图片
            com.bumptech.glide.Glide.with(this)
                .load(item.getImagePath())
                .placeholder(R.drawable.ic_add_photo)
                .error(R.drawable.ic_add_photo)
                .into(clothingImagePreview);
        }
    }
    
    private void saveClothing() {
        // 验证必填字段
        String name = editClothingName.getText().toString().trim();
        String typeStr = spinnerClothingType.getText().toString().trim();
        String color = spinnerColor.getText().toString().trim();
        
        if (TextUtils.isEmpty(name)) {
            editClothingName.setError("请输入衣物名称");
            editClothingName.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(typeStr)) {
            Toast.makeText(this, "请选择衣物类型", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(color)) {
            Toast.makeText(this, "请选择颜色", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // 创建或更新衣物对象
            ClothingItem item;
            if (isEditMode && editingItem != null) {
                item = editingItem;
            } else {
                item = new ClothingItem();
            }

            item.setName(name);
            item.setType(getClothingTypeByDisplayName(typeStr));
            item.setColor(color);

            // 安全地设置可选字段
            String brand = editBrand.getText() != null ? editBrand.getText().toString().trim() : "";
            String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";
            String material = editMaterial.getText() != null ? editMaterial.getText().toString().trim() : "";
            String size = editSize.getText() != null ? editSize.getText().toString().trim() : "";

            item.setBrand(brand);
            item.setDescription(description);
            item.setMaterial(material);
            item.setSize(size);
            item.setImagePath(currentImagePath);

            // 设置价格
            String priceStr = editPrice.getText() != null ? editPrice.getText().toString().trim() : "";
            if (!TextUtils.isEmpty(priceStr)) {
                try {
                    item.setPrice(Double.parseDouble(priceStr));
                } catch (NumberFormatException e) {
                    item.setPrice(0.0);
                }
            } else {
                item.setPrice(0.0);
            }

            // 设置季节
            List<Season> seasons = getSelectedSeasons();
            if (seasons.isEmpty()) {
                // 如果没有选择季节，默认设置为四季
                seasons = new ArrayList<>();
                seasons.add(Season.ALL_SEASON);
            }
            item.setSeasons(seasons);

            // 保存到数据库
            if (isEditMode) {
                // 更新现有衣物
                clothingViewModel.update(item).thenRun(() -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "衣物更新成功", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }).exceptionally(throwable -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "更新失败: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        throwable.printStackTrace();
                    });
                    return null;
                });
            } else {
                // 添加新衣物
                clothingViewModel.insert(item).thenRun(() -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "衣物添加成功", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }).exceptionally(throwable -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "添加失败: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        throwable.printStackTrace();
                    });
                    return null;
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "创建衣物对象失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private ClothingType getClothingTypeByDisplayName(String displayName) {
        for (ClothingType type : ClothingType.values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        return ClothingType.OTHER;
    }
    
    private List<Season> getSelectedSeasons() {
        List<Season> seasons = new ArrayList<>();

        for (int i = 0; i < seasonChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) seasonChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                int chipId = chip.getId();
                if (chipId == R.id.chip_season_spring) {
                    seasons.add(Season.SPRING);
                } else if (chipId == R.id.chip_season_summer) {
                    seasons.add(Season.SUMMER);
                } else if (chipId == R.id.chip_season_autumn) {
                    seasons.add(Season.AUTUMN);
                } else if (chipId == R.id.chip_season_winter) {
                    seasons.add(Season.WINTER);
                }
            }
        }

        return seasons;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
