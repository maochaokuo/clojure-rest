# A REST API in Clojure

origin

[A REST API in Clojure](https://tutswiki.com/rest-api-in-clojure/#google_vignette)
reference json

```json
{
      "id" : "some id",
      "title" : "some title",
      "text" : "some text"
}
```

## step 1
initialize the project
```shell
lein new compojure clojure-rest
```

then update project.clj

run the project
```shell
lein ring server
```

## open questions or next step
1. 接下來就改資料庫，可能的話還是用json server

2. kevin mai 的不能用，這個則不能debug, 能用但不是太理想。不然再
   把firefox開的tab都試試

## change log

### 2021/9/9
- 神奇了，這個是好的，kevin mai是壞的
- 可是這個不能debug，有點難用

### 2021/8/31
- 可以印出來了！讚

#
#
#
#
#
#
#
#
#
# clojure-rest

FIXME

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2021 FIXME
