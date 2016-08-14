(ns caesium.byte-bufs-benchmark
  (:require [caesium.crypto.secretbox :as sb]
            [caesium.crypto.secretbox-test :as st]
            [caesium.byte-bufs :as bb]
            [criterium.core :refer [bench]]
            [clojure.test :refer [deftest]]))

(defn secretbox-easy-wrap-byte-array
  [m n k]
  (let [c (byte-array (+ sb/macbytes (bb/buflen m)))]
    (sb/secretbox-easy-to-buf!
     (bb/->indirect-byte-buf c)
     (bb/->indirect-byte-buf m)
     (bb/->indirect-byte-buf n)
     (bb/->indirect-byte-buf k))
    c))

(defn secretbox-easy-unwrap-byte-buf
  [m n k]
  (let [c (bb/alloc (+ sb/macbytes (bb/buflen m)))]
    (sb/secretbox-easy-to-buf!
     c
     (bb/->indirect-byte-buf m)
     (bb/->indirect-byte-buf n)
     (bb/->indirect-byte-buf k))
    (bb/->bytes c)))

(deftest ^:benchmark wrap-byte-array
  (bench (secretbox-easy-wrap-byte-array st/ptext st/n0 st/secret-key)))

(deftest ^:benchmark unwrap-byte-buf
  (bench (secretbox-easy-unwrap-byte-buf st/ptext st/n0 st/secret-key)))
