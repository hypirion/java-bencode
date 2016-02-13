(ns com.hypirion.bencode.writer-test
  (:require [clojure.test :refer :all])
  (:import (com.hypirion.bencode BencodeWriter)
           (java.io ByteArrayOutputStream)))

(defn bencode
  "Returns a bencoded string of the values"
  [& vals]
  (let [baos (ByteArrayOutputStream.)
        out (BencodeWriter. baos)]
    (doseq [val vals]
      (.write out val))
    (.toString baos "UTF-8")))

(deftest write-integer
  (testing "that we correctly write bencoded integers"
    (are [x y] (= y (bencode x))
      3    "i3e"
      0    "i0e"
      -10  "i-10e"
      1234 "i1234e")))

(deftest write-string
  (testing "that we correctly write bencoded strings"
    (are [x y] (= y (bencode x))
        ""       "0:"
        ":"      "1::"
        "ie"     "2:ie"
        "foo"    "3:foo"
        "3:foo"  "5:3:foo"
        "plaît"  "6:plaît"
        "banana" "6:banana")))

(deftest write-list
  (testing "that we correctly write lists"
    (are [x y] (= y (bencode x))
      [] "le"
      [1] "li1ee"
      [1 2 3] "li1ei2ei3ee"
      [2 "foo"] "li2e3:fooe"
      [[]] "llee"
      [[] [1 2]] "lleli1ei2eee"
      [["foo"] "bar" ["baz"]] "ll3:fooe3:barl3:bazee")))

(deftest write-dict
  (testing "that we correctly write maps"
      (are [x y]  (= y (bencode x))
        {} "de"
        {"foo" "bar"} "d3:foo3:bare"
        {"foo" [{}]} "d3:fooldeee"
        {"x" {"y" {"z" 1}}} "d1:xd1:yd1:zi1eeee")))
