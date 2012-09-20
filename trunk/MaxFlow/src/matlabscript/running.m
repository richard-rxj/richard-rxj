cd D:\PhDWork\Jspace\MaxFlowSVN\test
%conSet = load('test-config.txt');

for i = 1
    p = 1;
    for j = 1
        %f = strcat(strcat('performance_',int2str(j)),'.txt');
        f='running.txt';
        C = load(f);
        N = C(:,1);
        CR = C(:,2);
        WR = C(:,3);
        plot(N,CR,'-*r',N,WR,':pb');
        set(gcf,'Position',[1 1 670 330]);
        legend('Garg and K.','TPath',1);
        xlabel('number of nodes');
        ylabel('running time(ms)');
        %title('Impact of \epsilon');
        %set(gca, 'XDir', 'reverse');
        p = p+1;
        %v = strcat('Performance',int2str(j));
        v='running';
        saveas(gcf,v,'pdf');
    end

end
