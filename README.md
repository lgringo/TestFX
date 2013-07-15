TestFX
======

Easy to use framework for testing JavaFX.

### Usage Examples

```java
click( "#foo" ).type( "Hello world!" );


assertThat( "#my-dialog .confirm-button", exists() )

assertThat( "#tweet-lane", contains( 2, ".tweet" ) );
```


### Hamcrest Matchers
Hamcrest matchers for JavaFX.


#### Usage Examples

```java
assertThat( "#my-dialog .confirm-button", exists() )

assertThat( "#tweet-lane", contains( 2, ".tweet" ) );
```

### Credits
TestFX was initially a part of [LoadUI][3], written by @dainnilsson. Today, it is being extended
and maintained by the LoadUI team.
