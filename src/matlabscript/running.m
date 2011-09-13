cd D:\PhDWork\Jspace\MaxFlowSVN\test\analysis-RE
%conSet = load('test-config.txt');

for i = 1
    p = 1;
    for j = [30,25,20,15]
        f = strcat(strcat('running_',int2str(j)),'.txt');
        %f='running.txt';
        C = load(f);
        N = C(:,1);
        CR = C(:,2);
        WR = C(:,3);
        subplot(2,2,p);
        plot(N,CR,'-*r',N,WR,':pb');
        set(gcf,'Position',[1 1 670 330]);
        legend('Garg and K.','TPath',2);
        xlabel('number of nodes');
        ylabel('running time(ms)');
        title(strcat('\epsilon = ',num2str(j/100)));
        %set(gca, 'XDir', 'reverse');
        set(gcf,'color',[1,1,1]); 
        p = p+1;
        %v = strcat('running',int2str(j));
        
        %v='running';
        %saveas(gcf,v,'pdf');
    end
    %r=getframe(gcf);
    %imwrite(r.cdata,'running.jpg');
end
