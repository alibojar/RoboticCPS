free[source(rescuer)].

!rescue.

+!rescue <- .wait(500);
			rescuer.a1(Inj);
			!check_end(Inj).

+!check_end(Inj): Inj =="end" <- 	!check_injureds;
									.wait(1000);
									!check_end(Inj).

+!check_end(Inj): Inj \=="end" <- .wait(500);
								.print("Found ", Inj);
								+injured(Inj);
								!check_injureds(Inj);
								!!rescue.

+!check_injureds : injured(Inj) <- !check_injureds(Inj).			

+!check_injureds : not injured(Inj) <- .broadcast(tell, end).	
								
+!check_injureds(Inj): free[source(Doc)] <- .print("Sending ", Inj, " to ", Doc);
												-free[source(Doc)];
												.send(Doc,achieve,go_to(Inj));
												+attending(Inj)[source(Doc)];
												-injured(Inj).
														 
+!check_injureds(Inj): not free[source(Doc)] <- 	.print("Broadcasting");
													.broadcast(tell,check(Inj)).
													


+discharge(Inj)[source(Doc)] <- 	-injured(Inj);
									-attending(Inj)[source(Doc)]; 
									-discharge(Inj)[source(Doc)].

+abandoned(Inj)[source(Doc)] <- 	-attending(Inj)[source(Doc)];
									+injured(Inj);
									-abandoned(Inj)[source(Doc)].



+free[source(Doc)]: true <- 	.print(Doc, " is free.");
								+team(Doc);
								.broadcast(tell, team(Doc)).

-free[source(Doc)] <- .print(Doc, " is busy.").										
