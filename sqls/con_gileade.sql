--update CEL_TIPOCELULA set VISIVEL = 1 where PERIODICIDADE = 'SEMANAL';
--SELECT * FROM CEL_TIPOCELULA WHERE OI = '2.5.' ;
--SELECT OI, DESCRICAO,TIPO_ID FROM CEL_CELULA WHERE OI = '2.5.';
--SELECT c.OI, c.DESCRICAO, c.TIPO_ID, t.DESCRICAO, t.VISIVEL FROM CEL_CELULA c INNER JOIN CEL_TIPOCELULA t ON c.TIPO_ID = t.ID WHERE t.VISIVEL = 1 AND c.OI = '2.5.';
--
--SELECT c.OI, c.DESCRICAO, c.TIPO_ID, t.DESCRICAO, t.VISIVEL, t.ATIVO FROM CEL_CELULA c INNER JOIN CEL_TIPOCELULA t ON c.TIPO_ID = t.ID WHERE c.OI = '2.5.';
--
--select * from CEL_FUNCAO WHERE OI = '2.5.';
--
--
--select * from CEL_TIPOCELULA;
--select DESCRICAO, ATIVO, TIPO_ID from CEL_CELULA;
--
--update PES_PESSOA set oi = '116.117.' where oi = '2.4.';
--
--delete from CEL_TIPOCELULA where ATIVO = 0;
--
--DROP TABLE CEL_TIPOCELULA;
--
--select d.ativo, d.DISCIPULADOR_ID, d.DISCIPULO_ID, p.NOME from DIS_DISCIPULADO d inner join PES_PESSOA p on p.ID = d.DISCIPULADOR_ID; 


--select dis.ATIVO, pes.NOME, (select pes2.nome from PES_PESSOA pes2 where pes2.ID = dis.DISCIPULO_ID) as nome2 from DIS_DISCIPULADO dis left join PES_PESSOA pes on pes.ID = dis.DISCIPULADOR_ID;
--select * from CEL_CELULA c where c.hierarquia like '6603.%';

