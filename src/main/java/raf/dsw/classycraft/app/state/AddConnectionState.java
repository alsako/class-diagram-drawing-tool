package raf.dsw.classycraft.app.state;

import raf.dsw.classycraft.app.commands.NewConnectionCommand;
import raf.dsw.classycraft.app.controller.actionsImpl.NewConnectionAction;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.view.painters.*;
import raf.dsw.classycraft.app.gui.swing.view.painters.connections.*;
import raf.dsw.classycraft.app.gui.swing.view.painters.interclasses.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.tree.ClassyTreeImpl;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.messagegen.Event;
import raf.dsw.classycraft.app.model.modelImpl.Diagram;
import raf.dsw.classycraft.app.model.modelImpl.classes.Interclass;
import raf.dsw.classycraft.app.model.modelImpl.connections.*;

import java.awt.*;
import java.util.List;

public class AddConnectionState implements ClassyState{

    ConnectionPainter cp = new ConnectionPainter();

    @Override
    public void misKliknut(Point p, DiagramView diagramView) {

        List<ElementPainter> diagramPainters = MainFrame.getInstance().getPackageView().getDiagramPainters().get(diagramView.getDiagram());

        if (NewConnectionAction.selectedOption==null){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(Event.OPTION_NOT_SELECTED);
            return;
        }

        for (ElementPainter painter:diagramPainters) {
            if (painter instanceof InterclassPainter && painter.elementAt(p.x, p.y)){
                Interclass from = (Interclass) painter.getElement();

                if (NewConnectionAction.selectedOption.equalsIgnoreCase("aggregation")){
                    Agregacija con = new Agregacija("a: " + from.getName() +"-", diagramView.getDiagram(), from);
                    AgregacijaPainter conPainter = new AgregacijaPainter(con);
                    con.addSubscriber(diagramView);
                    this.cp = conPainter; //ne zelim jos uvek da ga dodajem u stablo jer ne znam da li ce veza biti validna
                    MainFrame.getInstance().getPackageView().getDiagramPainters().get(diagramView.getDiagram()).add(conPainter);
                } else  if (NewConnectionAction.selectedOption.equalsIgnoreCase("composition")){
                    Kompozicija con = new Kompozicija("c: " + from.getName() +"-", diagramView.getDiagram(), from);
                    KompozicijaPainter conPainter = new KompozicijaPainter(con);
                    con.addSubscriber(diagramView);
                    this.cp = conPainter;
                    MainFrame.getInstance().getPackageView().getDiagramPainters().get(diagramView.getDiagram()).add(conPainter);
                } else  if (NewConnectionAction.selectedOption.equalsIgnoreCase("dependency")){
                    Zavisnost con = new Zavisnost("d: " + from.getName() +"-", diagramView.getDiagram(), from);
                    ZavisnostPainter conPainter = new ZavisnostPainter(con);
                    con.addSubscriber(diagramView);
                    this.cp = conPainter;
                    MainFrame.getInstance().getPackageView().getDiagramPainters().get(diagramView.getDiagram()).add(conPainter);
                } else  if (NewConnectionAction.selectedOption.equalsIgnoreCase("generalization")){
                    Generalizacija con = new Generalizacija("g: " + from.getName() +"-", diagramView.getDiagram(), from);
                    GeneralizacijaPainter conPainter = new GeneralizacijaPainter(con);
                    con.addSubscriber(diagramView);
                    this.cp = conPainter;
                    MainFrame.getInstance().getPackageView().getDiagramPainters().get(diagramView.getDiagram()).add(conPainter);
                } else return;

                return;

            }
        }
        ApplicationFramework.getInstance().getMessageGenerator().generateMessage(Event.CONNECTION_MUST_START_FROM_ENTITY);

    }

    @Override
    public void misPrevucen(Point p, DiagramView diagramView) {
        ConnectionPainter current = this.cp;
        Connection currentEl = (Connection) current.getElement();
        currentEl.setX(p.getX());
        currentEl.setY(p.getY());
    }

    @Override
    public void misOtpusten(Point p, DiagramView diagramView) {


        List<ElementPainter> diagramPainters = MainFrame.getInstance().getPackageView().getDiagramPainters().get(diagramView.getDiagram());
        diagramPainters.remove(diagramPainters.size() - 1); //skida poslednji u slucaju da se ne zavrsava na validnom mestu
        diagramView.repaint(); //pozivam repaint u slucaju da veza nije zavrsena na dobrom mestu da ne ostaje


            NewConnectionCommand newConnectionCommand = new NewConnectionCommand(p, diagramView, cp);
            diagramView.getCommandManager().addCommand(newConnectionCommand);

    }
}
