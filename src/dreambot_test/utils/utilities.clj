(ns dreambot-test.utils.utilities)

(import [java.util Random])

(def timeOutTime (int 330000)) ;; 5.5 minutes

(defn pollingTime
  "Generates a normally distrubted polling time along a guasian distribution"
  ([]
   (pollingTime 16000 10000)) ;; milliseconds
  ([mean stdev]
   (let [pollTime (+ (* (.nextGaussian (new Random)) stdev) mean)] ;; .nextGausian*stddev+mean to adjust the number to the desired mean & stdev
     (if (< 500 pollTime) ;; Polltimes < half a second are too fast
       (int pollTime)
       (recur mean stdev)))))
