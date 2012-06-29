(define nummap 
  (make-hashmap 1 "one" 2 "two" 3 "three"))
(print "nummap: " nummap)
(print (hashmap-set! nummap 2 "four?"))

(define symbolmap
  (make-hashmap 'one "one" 'two "two"))
(print "symbolmap: " symbolmap)
(print (hashmap-set! symbolmap  'one "three"))

(define stringmap
  (make-hashmap "one" 'one "two" 'two))
(print "stringmap: " stringmap)
(print (hashmap-set! stringmap "one" "three"))

(define charmap
  (make-hashmap #\a "a" #\b "b"))
(print "Charmap: " charmap)
(print (hashmap-set! charmap #\a "c"))
