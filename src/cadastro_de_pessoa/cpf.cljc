(ns cadastro-de-pessoa.cpf
  (:refer-clojure :exclude [format])
  (:require [cadastro-de-pessoa.shared :as shared]))

(def length 11)

;; A set of edge cases where the algorithm returns a false positive.
(def ^:private invalid-cpfs
  (conj (set (for [i (range 10)]
               (repeat length i))) [0 1 2 3 4 5 6 7 8 9 0]))

(defn valid?
  "Takes a string or seq of digits. Any characters that are not digits are ignored.
  Returns true if valid, else false"
  [cpf]
  (let [{:keys [digits control full]} (shared/parse cpf)]
    (and (= length (count full))
         (not (invalid-cpfs full))
         (= control (shared/control-digits (range 10 1 -1)
                                           (range 11 1 -1) digits)))))

(defn formatted?
  "Is the cpf formatted correctly?"
  [cpf]
  (boolean
   (re-find #"^[0-9]{3}\.[0-9]{3}\.[0-9]{3}-[0-9]{2}$" cpf)))

(defn format
  "Returns a string of the correctly formatted cpf"
  [cpf]
  (let [[a b c d e f g h i j k :as full] (:full (shared/parse cpf))]
    (assert (= length (count full)))
    (str a b c "." d e f "." g h i "-" j k)))