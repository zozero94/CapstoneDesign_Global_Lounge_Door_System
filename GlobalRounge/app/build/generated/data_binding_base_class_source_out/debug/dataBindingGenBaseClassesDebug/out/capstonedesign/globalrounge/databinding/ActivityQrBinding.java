package capstonedesign.globalrounge.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public abstract class ActivityQrBinding extends ViewDataBinding {
  @NonNull
  public final Button logout;

  @NonNull
  public final TextView textView;

  protected ActivityQrBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button logout, TextView textView) {
    super(_bindingComponent, _root, _localFieldCount);
    this.logout = logout;
    this.textView = textView;
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
