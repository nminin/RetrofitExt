1. Implementation

  Add it in your root build.gradle at the end of repositories:

  allprojects {
      repositories {
          maven { url 'https://jitpack.io' }
          google()
          jcenter()
      }
  }
  
  Add the dependency
  
  implementation 'com.github.nminin:RetrofitExt:0.1.1'
 
2 Using

Call.response(
  onSuccess = { code, item ->
  }, 
  onError = { code, message ->
  },
  //optional 
  onSuccessEmpty = { code ->
    //(if you will not set "onSuccessEmpty" and response empty, on error will be trigered)
  },
  //optional
  errorDeserializer = { code, responseBody ->
    //result of function will be delivered to onError
  }
)
