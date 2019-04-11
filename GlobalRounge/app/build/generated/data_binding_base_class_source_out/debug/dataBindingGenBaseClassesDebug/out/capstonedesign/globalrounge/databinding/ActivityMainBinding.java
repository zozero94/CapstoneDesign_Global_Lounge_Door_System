package capstonedesign.globalrounge.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public abstract class ActivityMainBinding extends ViewDataBinding {
  @NonNull
  public final CheckBox checkBox;

  @NonNull
  public final ConstraintLayout constraintLayoutMain;

  @NonNull
  public final EditText id;

  @NonNull
  public final LinearLayout idBlock;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final ImageView imageView2;

  @NonNull
  public final ImageView imageView3;

  @NonNull
  public final ImageView imageView4;

  @NonNull
  public final Button loginBtn;

  @NonNull
  public final LinearLayout loginLayout;

  @NonNull
  public final EditText pw;

  @NonNull
  public final LinearLayout pwBlock;

  protected ActivityMainBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, CheckBox checkBox, ConstraintLayout constraintLayoutMain, EditText id,
      LinearLayout idBlock, ImageView imageView, ImageView imageView2, ImageView imageView3,
      ImageView imageView4, Button loginBtn, LinearLayout loginLayout, EditText pw,
      LinearLayout pwBlock) {
    super(_bindingComponent, _root, _localFieldCount);
    this.checkBox = checkBox;
    this.constraintLayoutMain = constraintLayoutMain;
    this.id = id;
    this.idBlock = idBlock;
    this.imageView = imageView;
    this.imageView2 = imageView2;
    this.imageView3 = imageView3;
    this.imageView4 = imageView4;
    this.loginBtn = loginBtn;
    this.loginLayout = loginLayout;
    this.pw = pw;
    this.pwBlock = pwBlock;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, capstonedesign.globalrounge.R.layout.activity_main, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, capstonedesign.globalrounge.R.layout.activity_main, null, false, component);
  }

  public static ActivityMainBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityMainBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityMainBinding)bind(component, view, capstonedesign.globalrounge.R.layout.activity_main);
  }
}
