package io.gumga.reports.stimulsoft;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.envers.Audited;

@GumgaMultitenancy
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGAREPORT")
@Audited
@Entity
@Table(name = "gum_rep")
public class GumgaReport extends GumgaModel<Long> implements Serializable {

    private String name;
//    @Column(length = 16 * 1024)
    @Lob
    private String definition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}




/*
 
    <changeSet author="munif" id="mysql-241" dbms="mysql">
        <preConditions onFail="MARK_RAN" onFailMessage="A Tabela gum_rep já existe.">
            <not>
                <tableExists tableName="gum_rep"></tableExists>
            </not>
        </preConditions>
        <createTable tableName="gum_rep">
            <column autoIncrement="true" name="id" type="BIGINT">
                <constraints primaryKey="true"/>
            </column>
            <column name="oi" type="VARCHAR(255)"/>
            <column name="name" type="VARCHAR(255)">
                <constraints nullable="false"/>
            </column>
            <column name="definition" type="VARCHAR(16384)">
                <constraints nullable="true"/>
            </column>
        </createTable>
    </changeSet>
    <changeSet author="munif" id="mysql-242" dbms="mysql">
        <preConditions onFail="MARK_RAN" onFailMessage="A Tabela gum_rep_aud já existe.">
            <not>
                <tableExists tableName="gum_rep_aud"></tableExists>
            </not>
        </preConditions>
        <createTable tableName="gum_rep_aud">
            <column name="id" type="BIGINT">
                <constraints nullable="false"/>
            </column>
            <column name="rev" type="BIGINT">
                <constraints nullable="false"/>
            </column>
            <column name="revtype" type="TINYINT(3)"/>
            <column name="name" type="VARCHAR(255)">
            </column>
            <column name="definition" type="VARCHAR(16384)">
            </column>
        </createTable>
    </changeSet>

 */
