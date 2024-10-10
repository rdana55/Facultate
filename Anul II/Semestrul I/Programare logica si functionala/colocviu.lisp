;PREGATIRE COLOCVIU 

;POZA 1

;ex4

(defun exista(l e)
	(cond
		((null l) nil)
		((equal (car l) e) t)
		((listp (car l)) (or (exista (car l) e) (exista (cdr l) e)))
		(t (exista (cdr l) e))
	)
)

(defun drum (l e r)
	(cond
		((null l) nil)
		((equal (car l) e) r)
		((and (listp (car l))(exista (car l) e)) (drum (car l) e (append r (list (car (car l))) ) ))
		(t (drum (cdr l) e r))	
	)

)

(defun cale(l e)
	(drum l e (list (car l)))
)

;ex5

(defun par (e)
	(cond
		((null e) nil)
		((= (mod e 2) 0) t)
		(nil)
	)
)

(defun verificare (l)
	(cond
		((and (numberp (car l)) (par (car l))) nil)
		((numberp (car l)) t)
		(t (verificare (cdr l)))
	)
)

(defun nrsubimp (l r)
	(cond
		((null l) 0)
		((and (listp (car l))(verificare (car l))) (+ 1 r))
		(t (nrsubimp (cdr l) r))
	)
)

(defun sublisteimpar (l)
	(nrsubimp l 0)
)


(defun f(l)
	(cond
		((null l) nil)
		((listp (car l)) (append (f(car l)) (f (cdr l)) (car (f(car l)))))
		(t (list(car l)))
	)
)

(defun elimina (l e)
	(cond
		;((null l) nil)
		;((listp (atom (car l))) (append (elimina (car l) e)(elimina (cdr l) e) ))
		;((not (equal (atom (car l)) e)) (cons (car l) (elimina (cdr l) e)))
		;(t (elimina (cdr l) e))
		((and (atom l) (not (equal (atom l) e))) nil)
		(t (mapcar #'(lambda (l) (elimina l e)) l))
	)
)

(defun modif (l elem n)
	(cond
		((and(atom l) (equal (mod n 2) 0)) l)
		((and (atom l)(equal (mod n 2) 1)) elem)
		(t (mapcar #'(lambda (L) (modif L elem (+ 1 n)) ) l))
	)
)

(defun lgm(l)
	(cond
		((atom l) 0)
		(t (max (length l) (apply #'max (mapcar #'lgm l))))
	)
)



(defun f(l)
	(lambda (h) (cond
		((null l) 0)
		((> h 2) (+(car l) (f (cdr l)))) 
		(t h)  
	)) (f(car l))
)



(defun nivel (l k n)
	(cond
		((null l) nil)
		((and (atom l)(equal k n)) 0)
		((and (atom l)(not (equal k n))) l)
		(t (mapcar (lambda (x) (nivel x k (+ n 1)) ) l))
	)
)

(defun elim (l e)
	(cond
		((null l) nil)
		((and (atom l)(equal l e)) nil)
		((and (atom l)(not (equal l e))) l)
		(t (mapcar (lambda (x) (elim x e) ) l))
	)
)

(defun faranil (l)
	(cond
		((null l) nil)
		((equal (car l) nil) (faranil (cdr l)))
    		((listp (car l)) (cons (faranil (car l)) (faranil (cdr l))))
    		(t (cons (car l) (faranil (cdr l))))
	)
)

(defun mainEl (l e)
	(setq l (elim l e))
	(setq l (faranil l))
)

(defun inlocuire (l e n)
	(cond
		((null l) nil)
		((and (atom l)(equal (mod n 2) 1)) e)
		((and (atom l)(equal (mod n 2) 0)) l)
		(t (mapcar (lambda (x) (inlocuire x e (+ n 1)) ) l))
	)
)

(defun minn(a b)
	(cond
		((and (numberp a) (null b)) a)
		((and (numberp b) (null a)) b)
		((< a b) a)
		((< b a) b)
	)
)

(defun minim (l)
	(cond
		((null l) nil)
		((numberp l) l)
		(t (minim (append (cdr l) (minn (car l)(cadr l) ))) ))
	)
)














































