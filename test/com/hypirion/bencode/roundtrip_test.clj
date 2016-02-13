(ns com.hypirion.bencode.roundtrip-test
  (:require [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]])
  (:import (com.hypirion.bencode BencodeReader BencodeWriter)
           (java.io PipedInputStream PipedOutputStream)))

(defn- roundtrip [& vals]
  (with-open [sink (PipedInputStream.)
              source (PipedOutputStream. sink)
              input (BencodeReader. sink)
              output (BencodeWriter. source)]
    (let [res (take-while #(not (nil? %))
                          (repeatedly #(.read input)))]
      (future (dorun res))
      (doseq [val vals]
        (.write output val))
      (.close source)
      (doall res))))

(deftest basic-roundtrip
  (testing "that basic roundtripping works"
    (are [x] (= x (first (roundtrip x)))
      ":100"
      10
      [1 2 3]
      {"foo" "bar"}
      {"a" 10 "b" 20})))

(defn- nested-gen
  [inner-gen]
  (gen/one-of [(gen/list inner-gen)
               (gen/map gen/string inner-gen)]))
(def ^:private scalar-gen
  (gen/one-of [gen/int gen/string]))
(def ^:private bencodable-gen
  (gen/recursive-gen nested-gen scalar-gen))

(defspec test-check-roundtrips
  1000
  (prop/for-all [val bencodable-gen]
    (= val (first (roundtrip val)))))

(defspec test-check-stream-roundtrips
  100
  (prop/for-all [vals (gen/list bencodable-gen)]
    (= vals (apply roundtrip vals))))
