package capstonedesign.globalrounge.databinding;
import capstonedesign.globalrounge.R;
import capstonedesign.globalrounge.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityQrBindingImpl extends ActivityQrBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.imageView, 5);
        sViewsWithIds.put(R.id.qr, 6);
        sViewsWithIds.put(R.id.user_image, 7);
        sViewsWithIds.put(R.id.imageView2, 8);
        sViewsWithIds.put(R.id.logout, 9);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityQrBindingImpl(@Nullable android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private ActivityQrBindingImpl(android.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.support.constraint.ConstraintLayout) bindings[0]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.Button) bindings[9]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[2]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[1]
            );
        this.constraintLayoutMain.setTag(null);
        this.userCollage.setTag(null);
        this.userId.setTag(null);
        this.userMajor.setTag(null);
        this.userName.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.user == variableId) {
            setUser((capstonedesign.globalrounge.dto.Student) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setUser(@Nullable capstonedesign.globalrounge.dto.Student User) {
        this.mUser = User;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.user);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String UserName1 = null;
        java.lang.String userDepartment = null;
        java.lang.String userStudentID = null;
        capstonedesign.globalrounge.dto.Student user = mUser;
        java.lang.String userCollege = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (user != null) {
                    // read user.name
                    UserName1 = user.getName();
                    // read user.department
                    userDepartment = user.getDepartment();
                    // read user.studentID
                    userStudentID = user.getStudentID();
                    // read user.college
                    userCollege = user.getCollege();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.userCollage, userCollege);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.userId, userStudentID);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.userMajor, userDepartment);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.userName, UserName1);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): user
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}