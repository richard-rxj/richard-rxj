cd D:\PhDWork\Jspace\MaxFlowSVN\test\analysis-RE
%conSet = load('test-config.txt');

for i = 1
    p = 1;
    for j = [50,150,200,300]
        f = strcat(strcat('performance_',int2str(j)),'.txt');
        %f='performance.txt';
        C = load(f);
        N = C(:,1);
        CR = C(:,2);
        WR = C(:,4);
        subplot(2,2,p);
        plot(N,CR,'-*r',N,WR,':pb');
        set(gcf,'Position',[1 1 670 330]);
        legend('Garg and K.','TPath',2);
        xlabel('\epsilon');
        ylabel('flow(J/s)');
        f = strcat('n = ',int2str(j));
        title(f);
        set(gca, 'XDir', 'reverse');
        set(gcf,'color',[1,1,1]);
        p = p+1;
        %v = strcat('Performance',int2str(j));
        v='performance';
        saveas(gcf,v,'pdf');
    end

end
