(ns klaxon.monitor.net
  (:use [clojure.java.shell])
  (:require [clojure.string :as string]
            [clj-http.client :as client]
            [klaxon.monitor.util :as util]))

(defn =sep->map [l]
  (into {}
    (map 
     #(let [[k, v] (string/split % #"=")]
        [(keyword k), v]) l)))
    

(defn parse-ping [lines]
  (let [target  (zipmap [:host :ip] (rest (re-matches #"PING (?<host>[^\s]+) \((?<ip>[\d.]*)\).*" (first lines))))
        pings   (->> lines
                     (drop-last 4)
                     (drop 1)
                     (#(map (fn [l] 
                              (=sep->map
                                   (butlast
                                    (util/space-delimited 
                                     (second (string/split l #": ")))))) %))
                     (into []))
        [summary, stat] (take-last 2 lines)]
    (assoc target
      :pings pings
      :summary (zipmap [:transmitted :received :loss :time] (rest (re-matches #"(?<transmitted>[\d]+) packets transmitted, (?<received>[\d]+) received, (?<loss>[\d]+)% packet loss, time (?<time>[\d]+).*" summary)))
      :average (-> stat (string/split #" = ") second (string/split #"/") (#(zipmap [:min :avg :max :mdev] %))))))

(defn ping 
  ([host]
     (ping host 1))
  ([host count]
     (-> (sh "ping" "-c" (str count) host)
         :out
         string/split-lines
         parse-ping)))

(defn http-ok
  ([uri]
     (http-ok uri 200))
  ([uri status]
     (= (:status (client/get uri)) status)))

(defn http-content 
  [uri]
  (client/get uri))

