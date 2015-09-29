(ns klaxon.monitor.util
  (:require [clojure.string :as string]))

(defn space-delimited [line]
  (string/split line #"\s+"))

(defn lines->cols [lines]
  (map space-delimited lines))

(defn table->map 
  ([t]
   (table->map (map (comp keyword string/lower-case) (first t)) (rest t)))
  ([header rows]
     (map
      #(zipmap header %) rows)))

(defn pairs->map [pairs]
  (into {}
    (map
     #(let [kwd (keyword (string/lower-case (first %)))
            v   (rest %)]
        (if (> (count v) 1)
          {kwd (into [] v)}
          {kwd (first v)})) pairs)))
