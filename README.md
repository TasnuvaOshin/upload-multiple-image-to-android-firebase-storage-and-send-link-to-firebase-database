# upload-multiple-image-to-android-firebase-storage-and-send-link-to-firebase-database
upload image to firebase storage and send link to firebase database . Very Easy Steps 
to understand more about this please go to my youtube channel and learn about this. thanks


        public class MainActivity extends AppCompatActivity {


            private static final int PICK_IMG = 1;
            private ArrayList<Uri> ImageList = new ArrayList<Uri>();
            private int uploads = 0;
            private int index = 0;
            private DatabaseReference databaseReference;
            private ProgressDialog progressDialog;


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("User_one");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading ..........");
            }

            public void choose(View view) {
                //we will pick images
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMG);

            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == PICK_IMG) {
                    if (resultCode == RESULT_OK) {
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();

                            int CurrentImageSelect = 0;

                            while (CurrentImageSelect < count) {
                                Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                                ImageList.add(imageuri);
                                CurrentImageSelect = CurrentImageSelect + 1;
                            }

                            Toast.makeText(this, "You Have Selected " + ImageList.size() + " Images", Toast.LENGTH_SHORT).show();

                        }

                    }

                }

            }

            public void upload(View view) {
                progressDialog.show();
                final StorageReference ImageFolder =
                        FirebaseStorage.getInstance().getReference().child("ImageFolder");
                for (uploads=0; uploads < ImageList.size(); uploads++) {


                    ImageFolder.child(Objects.requireNonNull(ImageList.get(uploads).getLastPathSegment()))
                            .putFile(ImageList.get(uploads));

                    ImageFolder.child(Objects.requireNonNull(ImageList.get(uploads).getLastPathSegment())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            SendLink(url);

                        }
                    });






                }


            }

            private void SendLink(String url) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("link", url);
                databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        ImageList.clear();
                    }
                });


            }

        }














