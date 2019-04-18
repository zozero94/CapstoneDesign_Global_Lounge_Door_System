package capstonedesign.globalrounge.databinding;

import android.databinding.Bindable;
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
import android.widget.ImageView;
import android.widget.TextView;
import capstonedesign.globalrounge.dto.Student;

public abstract class ActivityQrBinding extends ViewDataBinding {
  @NonNull
  public final ConstraintLayout constraintLayoutMain;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final ImageView imageView2;

  @NonNull
  public final Button logout;

  @NonNull
  public final ImageView qr;

  @NonNull
  public final TextView userCollage;

  @NonNull
  public final TextView userId;

  @NonNull
  public final ImageView userImage;

  @NonNull
  public final TextView userMajor;

  @NonNull
  public final TextView userName;

  @Bindable
  protected Student mUser;

  protected ActivityQrBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ConstraintLayout constraintLayoutMain, ImageView imageView,
      ImageView imageView2, Button logout, ImageView qr, TextView userCollage, TextView userId,
      ImageView userImage, TextView userMajor, TextView userName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.constraintLayoutMain = constraintLayoutMain;
    this.imageView = imageView;
    this.imageView2 = imageView2;
    this.logout = logout;
    this.qr = qr;
    this.userCollage = userCollage;
    this.userId = userId;
    this.userImage = userImage;
    this.userMajor = userMajor;
    this.userName = userName;
  }

  public abstract void setUser(@Nullable Student user);

  @Nullable
  public Student getUser() {
    return mUser;
  }

  @NonNull
  public static ActivityQrBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityQrBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityQrBinding>inflate(inflater, capstonedesign.globalrounge.R.layout.activity_qr, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityQrBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityQrBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityQrBinding>inflate(inflater, capstonedesign.globalrounge.R.layout.activity_qr, null, false, component);
  }

  public static ActivityQrBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityQrBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityQrBinding)bind(component, view, capstonedesign.globalrounge.R.layout.activity_qr);
  }
}
