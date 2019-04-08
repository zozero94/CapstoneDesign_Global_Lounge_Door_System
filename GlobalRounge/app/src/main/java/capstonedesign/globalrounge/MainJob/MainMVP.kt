package capstonedesign.globalrounge.MainJob

interface MainMVP {
    interface View {
        fun noInformation(text: String)
        fun rejectPermission(text: String)
    }

    interface Presenter {
        fun loginClicked(id: String, pw: String)
        fun rejectPermission(text: String)
        fun approvalPermission(id: String)
    }

    interface Model {
        fun requestPermission(id: String, pw: String)
    }
}