(ns com.hypirion.bencode.roundtrip-test
  (:require [clojure.test :refer :all])
  (:import (com.hypirion.bencode BencodeReader BencodeWriter)
           (java.io PipedInputStream PipedOutputStream)))

(defn- roundtrip [& vals]
  (with-open [sink (PipedInputStream. (* 1024 1024))
              source (PipedOutputStream. sink)
              input (BencodeReader. sink)
              output (BencodeWriter. source)]
    (doseq [val vals]
      (.write output val))
    (.close source)
    (doall (take-while #(not (nil? %))
                       (repeatedly #(.read input))))))

(deftest basic-roundtrip
  (testing "that basic roundtripping works"
    (are [x] (= x (first (roundtrip x)))
      ":100"
      10
      [1 2 3]
      {"foo" "bar"})))
