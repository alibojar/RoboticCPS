free.

+free: .send(rescuer, tell, free).
severity_check(Inj) :- robot.severity(Inj,S) &
						injured(Inj_now,S_now) &
						S > S_now.

!inform.

+!inform: free <- .send(rescuer,tell,free).

+!injured_check(Inj) <- robot.severity(Inj,S);
						-+injured(Inj,S).

+!go_to(Inj)[source(Res)]	 <- -free;
								!injured_check(Inj);
								.print("Attending ", Inj);
								doctor2.a2(Inj, Done);
								.wait(1000);
								!has_attended(Inj).
							
							
								
+!has_attended(Inj): new_inj(New_inj) <- 	.print(Inj, " abandoned for ", New_inj);
											.send(rescuer,tell,abandoned(Inj));
											-new_inj(New_inj);
											!!go_to(New_inj).
						
+!has_attended(Inj): not new_inj(New_inj) <- 	.print(Inj, " ready for discharge.");
												.send(rescuer,tell,discharge(Inj));
												.send(rescuer,tell,free);
												-injured(Inj,S);
												+free.

+check(Inj)[source(Res)]: free <- !go_to(Inj)[source(Res)].
												
+check(Inj)[source(Res)]: severity_check(Inj) <- -+new_inj(Inj);
													.print("Severe injured to be attended: ", Inj);
													-check(Inj)[source(Res)].

+check(Inj)[source(Res)]: not severity_check(Inj) <- .print("Ignored ", Inj);
													-check(Inj)[source(Res)].
