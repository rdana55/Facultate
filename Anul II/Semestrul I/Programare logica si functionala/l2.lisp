;Problema 8

(defun inordine (x)
  	(cond
   	((null x) nil)
   	((atom x) (list x))
   	(t (append (inordine (cadr x)) (list (car x)) (inordine (caddr x))))
	)
)