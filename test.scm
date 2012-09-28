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

(define (bigcalc n)
  (let ((biglist (range2 1 n)))
    (let ((bigbiglist (concat biglist biglist)))
      (filter even?
        (map (lambda x (sqrt x))
          bigbiglist)))))

(define (bigcalc-i n)
  (let ((biglist (range2-i 1 n)))
    (let ((bigbiglist (concat biglist biglist)))
      (filter-i even?
        (map-i (lambda x (sqrt x))
          bigbiglist)))))

(define (imp-range start len)
  (let ((end (+ start len)))
    (let ((cur end)
        (accum '()))
      (while (> cur start)
        (set! cur (dec cur))
        (set! accum (cons cur accum)))
        accum)))
        
(define (printenv)
  (let ((env (car (globalEnv))))
    (let ((keys (hashmap-keys env)))
      (let ((keylen (vector-length keys))
            (i 0))
        (while (> keylen i)
          (let ((curkey (vector-ref keys i)))
            (print curkey #\: #\space (hashmap-get env curkey))
            (set! i (inc i))))))))

(define (contains?-test lst item)
  (let ((retval #f))
    (cond 
      ((list? lst)
        (while (not (empty? lst))
          (if (eq? (car lst) item)
            (begin
              (set! retval #t)
              (set! lst '()))
            (set! lst (cdr lst)))))
      ((vector? lst)
        (let ((size (vector-length lst))
              (i 0))
          (while (< i size)
            (if (eq? (vector-ref lst i) item)
              (begin
                (set! retval #t)
                (set! i (inc size)))
              (set! i (inc i))))))
        ((hashmap? lst)
          (set! retval (contains?-test (hashmap-keys lst) item))))
      retval))

;;Newton's approximation of a square root
(define (n-sqrt x)
	(newt-sqrt (/ x 2) x))

(define (newt-sqrt init-guess x)
  (let ((good-enough? (lambda (guess square)
                        (< (abs (- (* guess guess) square)) 0.0001))))
		(if (good-enough? init-guess x)
				init-guess
				(newt-sqrt (/ (+ init-guess (/ x init-guess)) 2.0)
									 x))))

(define (swing-test)
  (let ((panel (javax.swing.JPanel.))
        (greetButton (javax.swing.JButton. "Greet"))
        (nameField (javax.swing.JTextField. "World" 10))
        (frame (javax.swing.JFrame.)))
    (.add panel (javax.swing.JLabel. "Name: "))
    (.add panel nameField)
    (.add frame greetButton java.awt.BorderLayout/SOUTH)
    (.add frame panel java.awt.BorderLayout/CENTER)
    (.pack frame)
    (.setDefaultCloseOperation frame javax.swing.JFrame/EXIT_ON_CLOSE)
    (.setVisible frame #t)))


(define (factorial n)
	(if (= n 1)
			1
			(* n (factorial (- n 1)))))

(define (fact-iter x)
	(fact-iter-fn 1 1 x))

(define (fact-iter-fn acc step max)
	(if (> step max)
			acc
			(fact-iter-fn (* step acc)
										(+ step 1)
										max)))

(define (gcd-test a b)
	(if (= b 0)
			a
			(gcd-test b (remainder a b))))
