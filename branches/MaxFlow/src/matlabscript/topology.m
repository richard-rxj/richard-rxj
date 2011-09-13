cd D:\PhDWork\Jspace\MaxFlowSVN\test\topology;
%conSet = load('test-config.txt');
p=1;
for i=150
    v = strcat(strcat('vertex_',int2str(i)),'.txt');
    V = load(v);
    [x,y]=size(V);
    subplot(2,2,p);
    plot(V(:,2),V(:,3),'pb',V(1:1,2),V(1:1,3),'pr');
    xlabel('axisX');
    ylabel('axisY');
    title(strcat(int2str(i),' Nodes Topology'));
    %legend('sensor node','sink node',-1);
    p=p+1;
end
%v = 'Ctopology';
%saveas(gcf,v,'pdf');