(ns dreambot-test.core)

(import [org.dreambot.api.methods MethodProvider])

(def wait_time (int 1000))

(defn ^int onLoop
  []
  (MethodProvider/log "My test script")
  wait_time)

;; actions
;; wait times
;; click locaitons

;; click frequencies
