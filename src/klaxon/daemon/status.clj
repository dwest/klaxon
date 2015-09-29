(ns klaxon.daemon.status
  (:import [org.apache.commons.daemon Daemon DaemonContext])
  (:gen-class
    :implements [org.apache.commons.daemon.Daemon]))

(def state (atom {}))

(defn start []
  (while (:running @state)
    (println "tick")
    (Thread/sleep 2000)))

(defn init [args]
  (swap! state assoc :running true))

(defn stop []
  (swap! state assoc :running false))

(defn -start [this]
  (future (start)))

(defn -stop [this]
  (stop))

(defn -init [this context]
  (init (.getArguments context)))

(defn -main [& args]
  (init args)
  (start))
