MATCH (x0)<-[:p2]-()<-[:p1]-()-[:p1]->()-[:p2]->()<-[:p2]-(x1) RETURN DISTINCT x0, x1;
