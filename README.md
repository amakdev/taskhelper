# Task helper for Android

Execute background tasks simply with no leaks!
 
Implement singleton in application 

    public class MyApplication extends Application implements TaskHelperHolderApplication {

        private TaskHelper taskHelper;

        @Override
        public void onCreate() {
            super.onCreate();
            taskHelper = new TaskHelper(this);
        }

        @Override
        public TaskHelper getInstance() {
            return taskHelper;
        }

    }
    
Use inside you UI component

    public class MyIiComponent extends Acticity /* or Fragment */ implements TaskListener {
    
        private static final String TAG_EXAMPLE_TASK = "TAG_EXAMPLE_TASK";
    
        private LocalTaskExecutor taskExecutor;
        private ProgressDialog progressDialog;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            taskExecutor.registerListener(this);
        }
    
        public void executeTask() {
            Request request = new Request();
            taskExecutor.executeTask(TAG_EXAMPLE_TASK, request, new TaskRunnable<Request, Response>() {
                @Override
                public Integer execute(Request request) throws Exception {
                    ... do task
                    Response response = ...
                    return response;
                }
            });
        }

        @Override
        public void onTaskProgress(String componentTag, String taskTag, boolean inProgress) {
            if (inProgress) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                progressDialog.setCancelable(false);
            } else {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onTaskCompleted(String componentTag, String taskTag, Object resultObject) {
            Result result = (Result) resultObject;
            // handle result
            ...
        }

        @Override
        public void onTaskFailed(String componentTag, String taskTag, Exception ex) {
            // handle exception
            ...
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            taskExecutor.unregisterListener();
        }

    }