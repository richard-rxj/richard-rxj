cd D:\PhDWork\Jspace\MaxFlowSVN\test\analysis
%conSet = load('test-config.txt');

for i = 1
    p = 1;
    for j = [30,20,10]
        f = strcat(strcat('running_',int2str(j)),'.txt');
        %f='running.txt';
        C = load(f);
        N = C(:,1);
        CR = C(:,2);
        WR = C(:,3);
        subplot(3,1,p);
        plot(N,CR,'-*r',N,WR,':pb');
        set(gcf,'Position',[1 1 670 330]);
        legend('Garg and K.','TPath',1);
        xlabel('number of nodes');
        ylabel('running time(ms)');
        title(strcat('\epsilon = ',num2str(j/100)));
        %set(gca, 'XDir', 'reverse');
        p = p+1;
        %v = strcat('running',int2str(j));
        v='running';
        saveas(gcf,v,'pdf');
    end

end
