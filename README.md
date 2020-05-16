### Implementation

- Add it in your root build.gradle at the end of repositories:
>		allprojects { 
>      		repositories {
>          		maven { url 'https://jitpack.io' }
>          		google()
>          		jcenter()
>			}
>		}
- Add the dependency

> implementation 'com.github.nminin:RetrofitExt:0.1.1'
