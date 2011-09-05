cd D:\PhDWork\Jspace\MaxFlow\test;
for i=[6]
    v = strcat(strcat('vertex_',int2str(i)),'.txt');
    V = load(v);
    [x,y]=size(V);
    plot(V(:,2),V(:,3),'pb',V(1:1,2),V(1:1,3),'pr');
    xlabel('axisX');
    ylabel('axisY');
    axis([0,x,0,x]);
    title(strcat(int2str(i),' Nodes Topology'));
    legend('sensor node','sink node',-1);
    v = strcat('topology',int2str(i));
    saveas(gcf,v,'pdf');
end
