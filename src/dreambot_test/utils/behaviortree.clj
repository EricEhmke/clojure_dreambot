(ns dreambot-test.utils.behaviortree)

;; Sequence (evaluated in order, returns true or false)
;; a sequence is just a (and (leaf1 leaf2 leaf3)) since and stops at the first falsy value. It returns the last value it evaluates

;; action (a leaf, performs an action this is just a node based on abstractscript)
;; selection (perform a random one) this is just (rand-nth)
